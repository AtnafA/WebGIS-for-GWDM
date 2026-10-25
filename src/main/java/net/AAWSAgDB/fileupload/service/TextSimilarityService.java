package net.AAWSAgDB.fileupload.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class TextSimilarityService {

	 /**
     * Normalizes two strings and computes their similarity factor using Levenshtein metrics.
     * 
     * @param s1 First input text string
     * @param s2 Second input text string
     * @return A value between 0.0 (completely distinct) and 1.0 (identical match)
     */
    public double compute(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0.0;
        }

        // Normalize text configurations safely to prevent matching case errors
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0; // Both inputs are blank, so they share a 100% match factor
        }

        // Apply string distance calculation parameters
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        int distance = levenshtein.apply(s1, s2);

        // Compute percentage: (Max Length - Distance) / Max Length
        return (double) (maxLength - distance) / maxLength;
    }

}