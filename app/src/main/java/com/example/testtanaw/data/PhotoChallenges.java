package com.example.testtanaw.data;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PhotoChallenges {
    private PhotoChallenges() {
        // Private constructor to prevent instantiation
    }

    @NonNull
    public static final Map<Integer, List<String>> PHOTO_CHALLENGES;

    static {
        Map<Integer, List<String>> challenges = new HashMap<>();

        challenges.put(1, Collections.unmodifiableList(Arrays.asList(
                "Capture a community event supporting poverty alleviation.",
                "Photograph a local charity helping those in need.",
                "Show sustainable farming practices that reduce poverty.",
                "Take a photo of food distribution efforts.",
                "Capture people sharing resources in a community.",
                "Snap a picture of a small business empowering individuals.",
                "Photograph an education initiative addressing poverty.",
                "Show volunteers working to reduce poverty.",
                "Take a photo of affordable housing solutions.",
                "Capture a scene of people working together for change."
        )));

        challenges.put(2, Collections.unmodifiableList(Arrays.asList(
                "Photograph a community garden growing healthy food.",
                "Capture a meal program feeding the hungry.",
                "Show sustainable agriculture practices.",
                "Snap a photo of food waste reduction efforts.",
                "Photograph local farmers' markets supporting fresh produce.",
                "Capture a cooking class teaching healthy meals.",
                "Photograph food distribution during emergencies.",
                "Show a farm-to-table food initiative.",
                "Capture a food bank providing for those in need.",
                "Take a photo of volunteers serving food to the community."
        )));

        challenges.put(3, Collections.unmodifiableList(Arrays.asList(
                "Capture a community wellness event promoting health.",
                "Photograph a healthcare worker assisting patients.",
                "Show a fitness activity promoting physical health.",
                "Snap a photo of healthy meals being served.",
                "Photograph a vaccination drive or health clinic.",
                "Show mental health support services in action.",
                "Capture people exercising outdoors for well-being.",
                "Photograph a clean water source promoting health.",
                "Capture children receiving health education.",
                "Show a healthcare team working together."
        )));

        challenges.put(4, Collections.unmodifiableList(Arrays.asList(
                "Photograph a classroom with engaged students.",
                "Capture a teacher helping students learn.",
                "Show students using educational technology.",
                "Snap a photo of a community reading event.",
                "Capture a hands-on learning activity.",
                "Photograph a school garden project.",
                "Show children learning through play.",
                "Capture a library with students reading.",
                "Photograph a mentor guiding a student.",
                "Show students participating in a workshop."
        )));

        challenges.put(5, Collections.unmodifiableList(Arrays.asList(
                "Capture women leading a community event.",
                "Photograph boys and girls learning together.",
                "Show women in leadership positions at work.",
                "Snap a photo of a gender equality march.",
                "Capture a group discussion on gender issues."
        )));

        challenges.put(6, Collections.unmodifiableList(Arrays.asList(
                "Photograph a clean water source in the community.",
                "Capture a water purification process in action.",
                "Show people using clean water for daily needs.",
                "Photograph a sanitation facility promoting hygiene.",
                "Capture a community cleaning up water sources."
        )));

        challenges.put(7, Collections.unmodifiableList(Arrays.asList(
                "Capture solar panels powering a building.",
                "Photograph a wind turbine generating energy.",
                "Show energy-efficient lighting in use.",
                "Snap a photo of a community using clean energy.",
                "Capture people using renewable energy-powered devices."
        )));

        challenges.put(8, Collections.unmodifiableList(Arrays.asList(
                "Photograph a local business creating job opportunities.",
                "Capture workers in a safe, productive environment.",
                "Show a vocational training session in progress.",
                "Snap a photo of an entrepreneur at work.",
                "Capture a community event supporting economic growth."
        )));

        challenges.put(9, Collections.unmodifiableList(Arrays.asList(
                "Photograph a new technology in action.",
                "Capture workers building or improving infrastructure.",
                "Show a factory using sustainable practices.",
                "Snap a photo of innovative products being developed.",
                "Capture transportation infrastructure that promotes efficiency."
        )));

        challenges.put(10, Collections.unmodifiableList(Arrays.asList(
                "Capture a community event promoting inclusivity.",
                "Photograph diverse groups collaborating together.",
                "Show people with disabilities engaging in activities.",
                "Snap a photo of equal opportunities in education.",
                "Capture an initiative supporting marginalized communities."
        )));

        challenges.put(11, Collections.unmodifiableList(Arrays.asList(
                "Capture green spaces in urban areas.",
                "Photograph sustainable transportation in use.",
                "Show community recycling or waste management efforts.",
                "Snap a photo of eco-friendly buildings.",
                "Capture a public event promoting urban sustainability."
        )));

        challenges.put(12, Collections.unmodifiableList(Arrays.asList(
                "Photograph a zero-waste lifestyle in action.",
                "Capture eco-friendly packaging being used.",
                "Show sustainable food production practices.",
                "Snap a photo of people recycling.",
                "Capture a community repairing or upcycling items."
        )));

        challenges.put(13, Collections.unmodifiableList(Arrays.asList(
                "Capture a tree-planting event in progress.",
                "Photograph renewable energy sources in use.",
                "Show people reducing carbon footprints.",
                "Snap a photo of climate awareness campaigns.",
                "Capture a community preparing for climate change."
        )));

        challenges.put(14, Collections.unmodifiableList(Arrays.asList(
                "Photograph marine life in its natural habitat.",
                "Capture a clean-up effort at the beach.",
                "Show sustainable fishing practices in action.",
                "Snap a photo of coral reef conservation.",
                "Capture a boat or group protecting marine environments."
        )));

        challenges.put(15, Collections.unmodifiableList(Arrays.asList(
                "Capture wildlife in their natural habitat.",
                "Photograph a tree planting or reforestation project.",
                "Show a community working on land conservation.",
                "Snap a photo of sustainable farming practices.",
                "Capture biodiversity in a protected natural area."
        )));

        challenges.put(16, Collections.unmodifiableList(Arrays.asList(
                "Capture a peaceful community gathering.",
                "Photograph a local justice or legal service.",
                "Show people advocating for human rights.",
                "Snap a photo of government transparency in action.",
                "Capture a community conflict resolution event."
        )));

        challenges.put(17, Collections.unmodifiableList(Arrays.asList(
                "Photograph a collaborative community project.",
                "Capture a meeting between organizations working together.",
                "Show partnerships supporting sustainable development initiatives.",
                "Snap a photo of teamwork for global goals.",
                "Capture volunteers working with global organizations."
        )));

        PHOTO_CHALLENGES = Collections.unmodifiableMap(challenges);
    }
}
