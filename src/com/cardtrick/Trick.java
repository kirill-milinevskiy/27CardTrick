package com.cardtrick;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Trick {
    private List<String> heap0 = new ArrayList<>();
    private List<String> heap1 = new ArrayList<>();
    private List<String> heap2 = new ArrayList<>();

    private Set<String> cards = new HashSet<>(Arrays.asList(
            "9D", "10D", "JD", "QD", "KD", "AD",
            "9H", "10H", "JH", "QH", "KH", "AH",
            "9C", "10C", "JC", "QC", "KC", "AC",
            "9S", "10S", "JS", "QS", "KS", "AS",
            "8D", "8H", "8S"
    ));


    public static void main(String[] args) {
        Trick trick = new Trick();
        Scanner sc = new Scanner(System.in);

        List<String> shuffledCards = new ArrayList<>(trick.cards);
        Collections.shuffle(shuffledCards);

        System.out.println("Choose a card and remember your choice:");
        System.out.println(shuffledCards);

        System.out.println("\nAnd enter your favourite number from 0 to 26:");
        int favNum = sc.nextInt();
        Stack<Shift> shifts = trick.getShifts(favNum);

        System.out.println("OK! Lets go!");

        int i = 0;
        while (i < 3) {
            trick.heap0 = trick.getHeap(0, shuffledCards);
            trick.heap1 = trick.getHeap(1, shuffledCards);
            trick.heap2 = trick.getHeap(2, shuffledCards);


            System.out.println("What is a pile number where your card is?");
            System.out.println("0: " + trick.heap0);
            System.out.println("1: " + trick.heap1);
            System.out.println("2: " + trick.heap2);

            int pileNum = sc.nextInt();
            Shift shift = shifts.pop();
            shuffledCards = trick.getNewShuffledCards(shift, pileNum, trick.heap0, trick.heap1, trick.heap2);
            ++i;
        }

        System.out.println("And your card in the deck should be is under YOUR FAVOURITE number of " + favNum);

        i = 0;
        while (i < favNum) {
            System.out.println(i + ": " + shuffledCards.get(i));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++i;
        }

        System.out.println("Yes! This one!");
        System.out.println(i + ": " + shuffledCards.get(i));
    }

    private List<String> getNewShuffledCards(Shift shift, int pileNum, List<String> heap0, List<String> heap1, List<String> heap2) {
        List<String> shuffledCards = new ArrayList<>();
        switch (shift) {
            case TOP:
                if (pileNum == 0) {
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap2);
                }
                else if (pileNum == 1) {
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap2);
                }
                else if (pileNum == 2) {
                    shuffledCards.addAll(heap2);
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap1);
                }
                break;
            case MIDDLE:
                if (pileNum == 0) {
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap2);
                }
                else if (pileNum == 1) {
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap2);
                }
                else if (pileNum == 2) {
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap2);
                    shuffledCards.addAll(heap1);
                }
                break;
            case BOTTOM:
                if (pileNum == 0) {
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap2);
                    shuffledCards.addAll(heap0);
                }
                else if (pileNum == 1) {
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap2);
                    shuffledCards.addAll(heap1);
                }
                else if (pileNum == 2) {
                    shuffledCards.addAll(heap0);
                    shuffledCards.addAll(heap1);
                    shuffledCards.addAll(heap2);
                }
                break;
        }
        return shuffledCards;
    }


    private List<String> getHeap(int n, List<String> cards) {
        List<String> cardsSlice = cards.subList(n, cards.size());
        return IntStream.range(0, cardsSlice.size())
                .filter(i -> i % 3 == 0)
                .mapToObj(cardsSlice::get)
                .collect(Collectors.toList());
    }

    private Stack<Shift> getShifts(int favNum) {
        int nines = (int) Math.floor(favNum/9);
        int threes = (int) Math.floor((favNum%9)/3);
        int ones = favNum - nines*9 - threes*3;

        Stack<Shift> shifts = new Stack<>();
        shifts.push(nines == 0 ? Shift.TOP : (nines == 1 ? Shift.MIDDLE : Shift.BOTTOM));
        shifts.push(threes == 0 ? Shift.TOP : (threes == 1 ? Shift.MIDDLE : Shift.BOTTOM));
        shifts.push(ones == 0 ? Shift.TOP : (ones == 1 ? Shift.MIDDLE : Shift.BOTTOM));

        return shifts;
    }

    private enum Shift {
        TOP, MIDDLE, BOTTOM
    }
}
