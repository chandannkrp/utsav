package dev.utsav.api.dto;

public record MoneyView(
        long amount,
        String currency,
        String display
) {

    public static MoneyView ofPaise(long paise){
        String display = String.format("₹%.2f", paise / 100.0);
        return new MoneyView(paise, "INR", display);
    }
}
