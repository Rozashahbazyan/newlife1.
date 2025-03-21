package com.example.newlife;

import java.util.Random;

public class MotivationQuotes {

    private static final String[] quotes = {
            "Сегодня ваш день! Двигайтесь вперед, как Илон Маск!",
            "Не сдавайтесь! Успех ближе, чем вы думаете.",
            "Каждый маленький шаг приближает вас к большой цели.",
            "Ваше время ограничено, не тратьте его впустую.",
            "Мечтайте о большем, работайте усерднее, достигайте невозможного."
    };

    public static String getRandomQuote() {
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }
}
