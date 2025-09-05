package com.career.guide.backend.service;

import com.career.guide.backend.config.GeminiProperties;
import com.career.guide.backend.dto.quiz.QuizResponse;
import com.career.guide.backend.entity.OnboardingData;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.quiz.QuizQuestion;
import com.career.guide.backend.entity.quiz.QuizResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiApiService {

    private final RestClient restClient;
    private final GeminiProperties properties;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(GeminiApiService.class);

    public GeminiApiService(GeminiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder().baseUrl(properties.getBaseUrl()).build();
        this.objectMapper = new ObjectMapper();
    }

    private String buildIndustryInsightsPrompt(String industry) {
        return String.format("""
            Analyze the current state of the %s industry and provide insights in ONLY the following JSON format. Output MUST be valid JSON, no markdown, no prose, no comments. All fields are REQUIRED and must be non-empty; if exact figures are unavailable, provide best realistic estimates.
            {
              "salaryRanges": [
                { "role": "string", "min": number, "max": number, "median": number, "location": "string" }
              ],
              "growthRate": number,
              "demandLevel": "High" | "Medium" | "Low",
              "topSkills": ["skill1", "skill2", "skill3", "skill4", "skill5"],
              "marketOutlook": "Positive" | "Neutral" | "Negative",
              "keyTrends": ["trend1", "trend2", "trend3", "trend4", "trend5"],
              "recommendedSkills": ["skill1", "skill2", "skill3", "skill4", "skill5"]
            }
            Constraints:
            - Return ONLY JSON. No additional text, notes, or markdown formatting.
            - Include at least 5 common roles for salaryRanges, and set all numeric fields.
            - growthRate is a percentage number (e.g., 8.5). DemandLevel and marketOutlook must be one of the enumerated values.
            - topSkills, keyTrends, and recommendedSkills must each have at least 5 items.
            """, industry != null && !industry.isBlank() ? industry : "technology");
    }

    // Parse quiz questions from Gemini response. Supports either a wrapper object
    // {"questions": [...]} or a raw array [...]
    private List<QuizQuestion> parseQuizQuestions(String response) {
        try {
            Map<String, Object> wrapped = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            Object qs = wrapped.get("questions");
            List<Map<String, Object>> questionsData = objectMapper.convertValue(qs, new TypeReference<List<Map<String, Object>>>() {});
            return questionsData.stream().map(this::mapToQuizQuestion).toList();
        } catch (Exception e1) {
            try {
                List<Map<String, Object>> questionsData = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
                return questionsData.stream().map(this::mapToQuizQuestion).toList();
            } catch (Exception e2) {
                return List.of();
            }
        }
    }

    private QuizQuestion mapToQuizQuestion(Map<String, Object> data) {
        QuizQuestion q = new QuizQuestion();
        q.setQuestion((String) data.get("question"));
        q.setCorrectAnswer((String) data.get("correctAnswer"));
        q.setSkillCategory((String) data.get("skillCategory"));
        q.setExplanation((String) data.getOrDefault("explanation", ""));
        Object pointsObj = data.get("points");
        if (pointsObj instanceof Number n) {
            q.setPoints(n.intValue());
        } else {
            q.setPoints(10);
        }

        // Options as JSON string
        try {
            q.setOptions(objectMapper.writeValueAsString(data.get("options")));
        } catch (Exception e) {
            q.setOptions("[]");
        }

        // Difficulty mapping
        String difficulty = data.get("difficulty") != null ? data.get("difficulty").toString() : "EASY";
        if ("MEDIUM".equalsIgnoreCase(difficulty)) {
            q.setDifficulty(com.career.guide.backend.entity.enums.DifficultyLevel.MEDIUM);
        } else if ("HARD".equalsIgnoreCase(difficulty)) {
            q.setDifficulty(com.career.guide.backend.entity.enums.DifficultyLevel.HARD);
        } else {
            q.setDifficulty(com.career.guide.backend.entity.enums.DifficultyLevel.EASY);
        }

        return q;
    }

    public QuizResponse toQuizResponse(List<QuizQuestion> questions) {
        List<QuizResponse.Question> questionDtos = questions.stream().map(q -> {
            try {
                List<String> options = objectMapper.readValue(q.getOptions(), new TypeReference<List<String>>() {});
                return QuizResponse.Question.builder()
                    .id(q.getId())
                    .question(q.getQuestion())
                    .options(options)
                    .skillCategory(q.getSkillCategory())
                    .difficulty(q.getDifficulty().name())
                    .points(q.getPoints())
                    .build();
            } catch (Exception e) {
                return QuizResponse.Question.builder()
                    .id(q.getId())
                    .question(q.getQuestion())
                    .options(List.of())
                    .skillCategory(q.getSkillCategory())
                    .difficulty(q.getDifficulty().name())
                    .points(q.getPoints())
                    .build();
            }
        }).toList();

        return QuizResponse.builder()
            .questions(questionDtos)
            .timeLimit(15)
            .build();
    }

    private String buildPreparationPlanPrompt(String roadmapJson) {
        return String.format("""
            Generate a JSON representation of a week-wise career preparation plan, STRICTLY as a JSON array only, no extra text. The plan must be 100%% aligned to the following personal roadmap milestones array:

            ROADMAP: %s

            Requirements:
            - Output ONLY a valid JSON array. No markdown, no prose, no comments.
            - Minimum 15 weeks; each week must have at least 5 subpoints.
            - Each week object must have fields: week (number), title (string), data (array of { subpoint: string, youtube_link: string }).
            - youtube_link should be an embeddable YouTube URL (preferred: https://www.youtube.com/embed/VIDEO_ID), or a channel name, or empty string if unavailable.
            - Week content must exactly follow and align with the given roadmap modules in correct order.
            - Ensure links work in iframes (YouTube embed form preferred).

            Example schema only (do not include this example in output):
            [
              {
                "week": 1,
                "title": "Week 1 : Week Title",
                "data": [
                  {
                    "subpoint": "Description",
                    "youtube_link": "https://www.youtube.com/embed/zOjov-2OZ0E"
                  }
                ]
              }
            ]
            """, roadmapJson);
    }

    public List<QuizQuestion> generateQuizQuestions(User user, OnboardingData onboarding) {
        String prompt = buildQuizPrompt(user, onboarding);
        String response = callGemini(prompt);
        return parseQuizQuestions(response);
    }

    public String generateRoadmap(User user, OnboardingData onboarding, QuizResult quizResult) {
        String prompt = buildRoadmapPrompt(user, onboarding, quizResult);
        return callGemini(prompt);
    }

    public String generateIndustryInsights(String industry) {
        String prompt = buildIndustryInsightsPrompt(industry);
        return callGemini(prompt);
    }

    public String generateWeeklyPlan(String roadmapData, int weekNumber) {
        String prompt = buildWeeklyPlanPrompt(roadmapData, weekNumber);
        return callGemini(prompt);
    }

    public String generatePreparationPlanFromRoadmap(String roadmapJson) {
        String prompt = buildPreparationPlanPrompt(roadmapJson);
        return callGemini(prompt);
    }

    public String generateImprovementTip(String industry, String wrongQuestionsText) {
        String prompt = String.format("""
            The user made the following mistakes in a %s technical quiz. Provide a concise, specific improvement tip in <= 2 sentences, encouraging tone, no explicit mention of the mistakes; focus on what to learn/practice next.

            %s
            """, industry != null && !industry.isBlank() ? industry : "tech", wrongQuestionsText != null ? wrongQuestionsText : "");
        return callGemini(prompt);
    }

    private String callGemini(String prompt) {
        String modelPath = properties.getModel();
        // Support either 'gemini-1.5-pro' or 'models/gemini-1.5-pro' in configuration
        if (modelPath.startsWith("models/")) {
            modelPath = modelPath.substring("models/".length());
        }
        String url = "/models/" + modelPath + ":generateContent?key=" + properties.getKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", prompt))
            )),
            "generationConfig", Map.of(
                "temperature", 0.7,
                "maxOutputTokens", 4096,
                "response_mime_type", "application/json"
            )
        );

        try {
            var response = restClient.post()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .body(body)
                .retrieve()
                .toEntity(Map.class)
                .getBody();

            if (response != null && response.containsKey("candidates")) {
                var candidates = (List<?>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    var candidate = (Map<?, ?>) candidates.get(0);
                    Object contentObj = candidate.get("content");
                    if (contentObj instanceof Map<?, ?> content) {
                        var parts = (List<?>) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            var part = (Map<?, ?>) parts.get(0);
                            var text = (String) part.get("text");
                            if (text == null) {
                                log.warn("Gemini response has empty text in parts: {}", response);
                                return "";
                            }
                            log.debug("Gemini response text length: {}", text.length());
                            return text;
                        }
                    }
                    Object prediction = candidate.get("output_text");
                    if (prediction instanceof String s && !s.isBlank()) {
                        return s;
                    }
                }
            }
            log.warn("Unexpected Gemini response shape or empty candidates: {}", response);
            return "";
        } catch (RestClientResponseException re) {
            log.error("Gemini HTTP error: status={} body={}", re.getRawStatusCode(), re.getResponseBodyAsString(), re);
            return "";
        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage(), e);
            return "";
        }
    }

    private String buildQuizPrompt(User user, OnboardingData onboarding) {
        return String.format("""
            You are an expert assessment designer. Create a STRICTLY JSON-ONLY personalized technical quiz for the following user. Return ONLY JSON, no prose/markdown.

            USER CONTEXT
            - branchOrField: %s
            - careerGoal: %s
            - currentSkills: %s
            - skillLevels: %s
            - targetCompanies: %s
            - preferredRoles: %s

            REQUIREMENTS
            - Exactly 10 multiple-choice questions.
            - Each question must include: question, options (array of 4), correctAnswer, skillCategory, difficulty in [EASY, MEDIUM, HARD], points (integer), explanation.
            - Calibrate difficulty distribution based on skillLevels; include at least 3 MEDIUM and 2 HARD where appropriate.
            - Questions should reflect the user's currentSkills and preferredRoles, and align with targetCompanies expectations.
            - Output must be a JSON OBJECT with a single key "questions" whose value is an array of question objects.

            OUTPUT EXAMPLE SHAPE (do not include this example text in output):
            {
              "questions": [
                {"question":"...","options":["A","B","C","D"],"correctAnswer":"A","skillCategory":"DSA","difficulty":"MEDIUM","points":10,"explanation":"..."}
              ]
            }
            """,
            onboarding != null && onboarding.getBranch() != null ? onboarding.getBranch() : "Computer Science",
            onboarding != null && onboarding.getCareerGoal() != null ? onboarding.getCareerGoal() : "Software Developer",
            onboarding != null && onboarding.getCurrentSkills() != null ? onboarding.getCurrentSkills() : "Programming basics",
            onboarding != null && onboarding.getSkillLevels() != null ? onboarding.getSkillLevels() : "Beginner",
            onboarding != null && onboarding.getTargetCompanies() != null ? onboarding.getTargetCompanies() : "Tech companies",
            onboarding != null && onboarding.getPreferredRoles() != null ? onboarding.getPreferredRoles() : "Software Engineer"
        );
    }

    private String buildRoadmapPrompt(User user, OnboardingData onboarding, QuizResult quizResult) {
        return String.format("""
            You are an expert career planner. Create a STRICT, COMPLETE, and PERSONALIZED learning roadmap for the user described below. The roadmap must be returned as a JSON ARRAY ONLY, no prose, no markdown, no comments.

            USER CONTEXT
            - targetGoal: %s
            - currentProgressNote: %s
            - branchOrField: %s
            - currentSkills: %s
            - targetCompanies: %s
            - preferredRoles: %s
            - targetSalary: %s
            - dailyStudyHours: %s
            - quizAssessmentScore: %s

            HARD REQUIREMENTS
            - Output MUST be valid JSON (double quotes, no trailing commas), and be ONLY a JSON array of milestones.
            - Produce between 10 and 18 milestones inclusive (minimum 10). If the user is advanced, bias toward the higher end.
            - Personalize all content using the USER CONTEXT.
            - Sequence milestones from beginner to advanced based on the user's current skills and score.
            - Include at least: fundamentals, data structures & algorithms, systems/CS concepts (as applicable), core tech stack, projects, portfolio, interview prep, and final capstone aligned to the target goal.
            - Ensure realistic workload calibrated by dailyStudyHours and include estimated time.

            OUTPUT SCHEMA (array of objects)
            [
              {
                "title": "string",
                "sub title": "string",
                "description": "string",
                "durationWeeks": number,
                "prerequisites": ["string"],
                "resources": [                       
                  { "type": "course|docs|video|tool|book", "title": "string", "url": "string", "free": true }
                ],
                "dependencies": ["string"],        
                "tags": ["string"],                
                "documentationLink": "string",     
                "completed": false                   
              }
            ]

            CONSTRAINTS AND GUIDANCE
            - Use concrete, actionable milestones (avoid generic advice). Include at least one hands-on project every 2-3 milestones.
            - Calibrate difficulty and duration using quizAssessmentScore and currentSkills.
            - If any USER CONTEXT field is missing or generic, infer sensible defaults and continue.
            - For resources, prefer reputable, up-to-date links. Mark "free": true when the resource is free.
            - The final milestone must be a capstone aligned to targetGoal with a showcase/portfolio delivery.
            - Do NOT include any explanation outside of the JSON array.
            """,
            onboarding.getCareerGoal() != null ? onboarding.getCareerGoal() : "Software Developer",
            user.getUserProfile() != null && user.getUserProfile().getBio() != null ? user.getUserProfile().getBio() : "Starting from basics",
            onboarding.getBranch() != null ? onboarding.getBranch() : "Computer Science",
            onboarding.getCurrentSkills() != null ? onboarding.getCurrentSkills() : "Basic programming",
            onboarding.getTargetCompanies() != null ? onboarding.getTargetCompanies() : "Tech companies",
            onboarding.getPreferredRoles() != null ? onboarding.getPreferredRoles() : "Software Engineer",
            onboarding.getTargetSalary() != null ? onboarding.getTargetSalary() : 50000,
            onboarding.getDailyStudyHours() != null ? onboarding.getDailyStudyHours() : 2,
            quizResult != null ? quizResult.getTotalScore() : 0
        );
    }

    private String buildWeeklyPlanPrompt(String roadmapData, int weekNumber) {
        return String.format("""
            Based on this roadmap data:
            %s
            
            Generate a detailed weekly plan for Week %d with:
            1. Learning objectives for the week
            2. Daily tasks (Monday to Sunday)
            3. Resources (videos, articles, practice problems)
            4. Milestones to achieve
            5. Time allocation per task
            
            Return as structured JSON with clear daily breakdown and actionable items.
            """, roadmapData, weekNumber);
    }
}