package com.geekbrains.lesson_4;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static final String BG_BLACK = "\u001B[40m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_WHITE = "\u001B[47m";
    private static final String TEXT_BLACK = "\u001B[30m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RND = new Random();
    private static char[][] GAME_MAP;
    private static int MAP_SIZE = mapSizeChoice();
    private static final char DOT_EMPTY = '.';
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';
    private static char USER_DOT;
    private static char AI_DOT;

    private static boolean RND_TURN_MODE = gameModeChoice(); // Включить/выключить случайные ходы компьютера (легкий режим игры)

    public static void main(String[] args) {
        chooseYourDot();
        initMap();
        printMap();

        while (true) {
            if (USER_DOT == DOT_X) {
                humanTurn(); // ход игрока
                if (isEndGame(DOT_X)) {
                    break;
                }
                compTurn(); // ход компьютера
                if (isEndGame(DOT_O)) {
                    break;
                }
            } else {
                compTurn(); // ход компьютера
                if (isEndGame(DOT_O)) {
                    break;
                }
                humanTurn(); // ход игрока
                if (isEndGame(DOT_X)) {
                    break;
                }
            }
        }
        System.out.println(BG_WHITE + TEXT_BLACK + "Конец игры" + COLOR_RESET);
    }


    public static void chooseYourDot() {
        System.out.print("Выбери, чем будешь ходить (\"O\" - 0, \"X\" -  1) - ");
        int dotChoice;
        while (true) { //повторяем, пока пользователь не сделает корректный ввод
            try {
                dotChoice = Integer.parseInt(SCANNER.nextLine());
                if (dotChoice == 1) {
                    USER_DOT = DOT_X;
                    AI_DOT = DOT_O;
                    break;
                }
                if (dotChoice == 0) {
                    USER_DOT = DOT_O;
                    AI_DOT = DOT_X;
                    break;
                }
                if (dotChoice != 0 && dotChoice != 1) {
                    System.out.print("Ошибка ввода. Повтори выбор - ");
                    continue;
                }
            } catch (Exception e) {
                System.out.print("Ошибка ввода. Повтори выбор - ");
            }
        }
    }

    public static int mapSizeChoice() {
        System.out.print("Выбери размер игрового поля (3 или 5) - ");
        int size;
        while (true) { //повторяем, пока пользователь не сделает корректный ввод
            try {
                size = Integer.parseInt(SCANNER.nextLine());
                if (size == 3 || size == 5) {
                    System.out.printf(BG_YELLOW + TEXT_BLACK + "Крестики-нолики %sх%s\n", size, size + COLOR_RESET);
                    break;
                }
                if (size != 3 && size != 5) {
                    System.out.print("Ошибка ввода. Повтори выбор - ");
                    continue;
                }
            } catch (Exception e) {
                System.out.print("Ошибка ввода. Повтори выбор - ");
            }
        }
        return size;
    }

    public static boolean gameModeChoice() {
        if (MAP_SIZE == 3) {
            System.out.print("Выбери сложность игры (легко - 0, сложно - 1) - ");
            int difficult;
            while (true) { //повторяем, пока пользователь не сделает корректный ввод
                try {
                    difficult = Integer.parseInt(SCANNER.nextLine());
                    if (difficult == 0 || difficult == 1) {
                        break;
                    }
                    if (difficult != 0 && difficult != 1) {
                        System.out.print("Ошибка ввода. Повтори выбор - ");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.print("Ошибка ввода. Повтори выбор - ");
                }
            }
            if (difficult == 1) {
                System.out.println("Выбран сложный режим");
                return false;
            } else {
                System.out.println("Выбран легкий режим");
            }
        }
        return true;
    }

    private static void initMap() {
        GAME_MAP = new char[MAP_SIZE][MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                GAME_MAP[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printMap() {
        for (int i = 0; i <= MAP_SIZE; i++) {
            System.out.print(BG_BLACK + i + " " + COLOR_RESET);
        }
        System.out.println();

        for (int i = 0; i < MAP_SIZE; i++) {
            System.out.print(BG_BLACK + (i + 1) + BG_WHITE + " " + COLOR_RESET);
            for (int j = 0; j < MAP_SIZE; j++) {
                System.out.print(BG_WHITE + TEXT_BLACK + GAME_MAP[i][j] + " " + COLOR_RESET);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Сделайте ход (введите координаты ячейки через пробел):");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x, y));
        GAME_MAP[x][y] = USER_DOT;
    }

    private static void compTurn() {
        if (MAP_SIZE != 3) {
            RND_TURN_MODE = true; // "Умный компьютер" работает только с полем 3х3
        }
        int x;
        int y;
        int[] compTurnCoords;
        if (RND_TURN_MODE) {
            do {
                x = RND.nextInt(MAP_SIZE);
                y = RND.nextInt(MAP_SIZE);
            } while (!isCellValid(x, y));
        } else {
            do {
                compTurnCoords = compTurnAdvanced(GAME_MAP);
                x = compTurnCoords[0];
                y = compTurnCoords[1];
            } while (!isCellValid(x, y));
        }
        System.out.println("Компьютер выбрал ячейку " + (x + 1) + " " + (y + 1));
        GAME_MAP[x][y] = AI_DOT;
    }

    private static boolean isCellValid(int x, int y) {
        boolean result = true;
        if (x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) {
            result = false;
        }
        if (GAME_MAP[x][y] != DOT_EMPTY) {
            result = false;
        }
        return result;
    }

    private static boolean isEndGame(char playerSymbol) {
        boolean result = false;
        printMap();
        if (checkWin(playerSymbol)) {
            System.out.println("Победили " + playerSymbol);
            result = true;
        }
        if (isMapFull()) {
            System.out.println("Ничья");
            result = true;
        }
        return result;
    }

    private static boolean isMapFull() {
        boolean result = true;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (GAME_MAP[i][j] == DOT_EMPTY)
                    result = false;
            }
        }
        return result;
    }

    private static boolean checkWin(char playerSymbol) {
        boolean result = false;
        // проверка строк на победные комбинации
        for (int i = 0; i < MAP_SIZE; i++) {
            int combinationToWin = 0;
            for (int j = 0; j < MAP_SIZE; j++) {
                if (GAME_MAP[i][j] == playerSymbol) {
                    combinationToWin++;
                } else {
                    combinationToWin = 0;
                }
                if ((MAP_SIZE == 3 && combinationToWin == 3) || (MAP_SIZE == 5 && combinationToWin == 4)) {
                    result = true;
                    break;
                }
            }
            if (result == true) {
                break;
            }
        }

        // проверка столбцов на победные комбинации
        for (int i = 0; i < MAP_SIZE; i++) {
            int combinationToWin = 0;
            for (int j = 0; j < MAP_SIZE; j++) {
                if (GAME_MAP[j][i] == playerSymbol) {
                    combinationToWin++;
                } else {
                    combinationToWin = 0;
                }
                if ((MAP_SIZE == 3 && combinationToWin == 3) || (MAP_SIZE == 5 && combinationToWin == 4)) {
                    result = true;
                    break;
                }
            }
            if (result == true) {
                break;
            }
        }

        // проверка диагонали_1 на победные комбинации
        int combinationToWin_diagonal_1 = 0;
        for (int i = 0; i < MAP_SIZE; i++) {
            if (GAME_MAP[i][i] == playerSymbol) {
                combinationToWin_diagonal_1++;
            } else {
                combinationToWin_diagonal_1 = 0;
            }
            if ((MAP_SIZE == 3 && combinationToWin_diagonal_1 == 3) || (MAP_SIZE == 5 && combinationToWin_diagonal_1 == 4)) {
                result = true;
                break;
            }
        }

        // проверка диагонали_2 на победные комбинации
        int combinationToWin_diagonal_2 = 0;
        for (int i = 0; i < MAP_SIZE; i++) {
            if (GAME_MAP[i][MAP_SIZE - i - 1] == playerSymbol) {
                combinationToWin_diagonal_2++;
            } else {
                combinationToWin_diagonal_2 = 0;
            }
            if ((MAP_SIZE == 3 && combinationToWin_diagonal_2 == 3) || (MAP_SIZE == 5 && combinationToWin_diagonal_2 == 4)) {
                result = true;
                break;
            }
        }

        // проверка короткой диагонали_1 в поле 5х5
        if (MAP_SIZE == 5) {
            int combinationToWin_short_diagonal_1 = 0;
            for (int i = 1; i < 5; i++) {
                if (GAME_MAP[i][i - 1] == playerSymbol) {
                    combinationToWin_short_diagonal_1++;
                }
            }
            if (combinationToWin_short_diagonal_1 == 4) {
                result = true;
            }

            int combinationToWin_short_diagonal_2 = 0;
            for (int i = 0; i < 4; i++) {
                if (GAME_MAP[i][i + 1] == playerSymbol) {
                    combinationToWin_short_diagonal_2++;
                }
            }
            if (combinationToWin_short_diagonal_2 == 4) {
                result = true;
            }

            int combinationToWin_short_diagonal_3 = 0;
            for (int i = 0; i < 4; i++) {
                if (GAME_MAP[i][3 - i] == playerSymbol) {
                    combinationToWin_short_diagonal_3++;
                }
            }
            if (combinationToWin_short_diagonal_3 == 4) {
                result = true;
            }

            int combinationToWin_short_diagonal_4 = 0;
            for (int i = 1; i < 5; i++) {
                if (GAME_MAP[i][5 - i] == playerSymbol) {
                    combinationToWin_short_diagonal_4++;
                }
            }
            if (combinationToWin_short_diagonal_4 == 4) {
                result = true;
            }

        }
        return result;
    }

    // продвинутый алгоритм хода компьютера
    public static int[] compTurnAdvanced(char[][] map) {
        int[] turnCoords = new int[2];
        int isTurnDone = 0;
        while (true) {
            // ход в центр, если свободно
            if (map[1][1] == DOT_EMPTY) {
                turnCoords[0] = 1;
                turnCoords[1] = 1;
                isTurnDone = 1;
            }
            if (isTurnDone == 1) break;

            //проверка строк на возможность победить следующим ходом
            for (int i = 0; i < 3; i++) {
                int lineAIWeight = 0;
                for (int j = 0; j < 3; j++) {
                    if (map[i][j] == AI_DOT) {
                        lineAIWeight++;
                        if (lineAIWeight == 2) {
                            for (int k = 0; k < 3; k++) {
                                if (map[i][k] == DOT_EMPTY) {
                                    turnCoords[0] = i;
                                    turnCoords[1] = k;
                                    isTurnDone = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (isTurnDone == 1) break;
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            //проверка колонок на возможность победить следующим ходом
            for (int i = 0; i < 3; i++) {
                int columnAIWeight = 0;
                for (int j = 0; j < 3; j++) {
                    if (map[j][i] == AI_DOT) {
                        columnAIWeight++;
                        if (columnAIWeight == 2) {
                            for (int k = 0; k < 3; k++) {
                                if (map[k][i] == DOT_EMPTY) {
                                    turnCoords[0] = k;
                                    turnCoords[1] = i;
                                    isTurnDone = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (isTurnDone == 1) break;
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка диагонали 1 на возможность победить следующим ходом
            int diagonal_1_AIWeight = 0;
            for (int i = 0; i < 3; i++) {
                if (map[i][i] == AI_DOT) {
                    diagonal_1_AIWeight++;
                    if (diagonal_1_AIWeight == 2) {
                        for (int j = 0; j < 3; j++) {
                            if (map[j][j] == DOT_EMPTY) {
                                turnCoords[0] = j;
                                turnCoords[1] = j;
                                isTurnDone = 1;
                                break;
                            }
                        }
                    }
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка диагонали 2 на возможность победить следующим ходом
            int diagonal_2_AIWeight = 0;
            for (int i = 0; i < 3; i++) {
                if (map[i][2 - i] == AI_DOT) {
                    diagonal_2_AIWeight++;
                    if (diagonal_2_AIWeight == 2) {
                        for (int j = 0; j < 3; j++) {
                            if (map[j][2 - j] == DOT_EMPTY) {
                                turnCoords[0] = j;
                                turnCoords[1] = 2 - j;
                                isTurnDone = 1;
                                break;
                            }
                        }
                    }
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка строк на необходимость закрыть победную комбинацию соперника
            for (int i = 0; i < 3; i++) {
                int lineUserWeight = 0;
                for (int j = 0; j < 3; j++) {
                    if (map[i][j] == USER_DOT) {
                        lineUserWeight++;
                        if (lineUserWeight == 2) {
                            for (int k = 0; k < 3; k++) {
                                if (map[i][k] == DOT_EMPTY) {
                                    turnCoords[0] = i;
                                    turnCoords[1] = k;
                                    isTurnDone = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (isTurnDone == 1) break;
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка колонок на необходимость закрыть победную комбинацию соперника
            for (int i = 0; i < 3; i++) {
                int columnUserWeight = 0;
                for (int j = 0; j < 3; j++) {
                    if (map[j][i] == USER_DOT) {
                        columnUserWeight++;
                        if (columnUserWeight == 2) {
                            for (int k = 0; k < 3; k++) {
                                if (map[k][i] == DOT_EMPTY) {
                                    turnCoords[0] = k;
                                    turnCoords[1] = i;
                                    isTurnDone = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (isTurnDone == 1) break;
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка диагонали 1 на необходимость закрыть победную комбинацию соперника
            int diagonal_1_UserWeight = 0;
            for (int i = 0; i < 3; i++) {
                if (map[i][i] == USER_DOT) {
                    diagonal_1_UserWeight++;
                    if (diagonal_1_UserWeight == 2) {
                        for (int j = 0; j < 3; j++) {
                            if (map[j][j] == DOT_EMPTY) {
                                turnCoords[0] = j;
                                turnCoords[1] = j;
                                isTurnDone = 1;
                                break;
                            }
                        }
                    }
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // проверка диагонали 2 на необходимость закрыть победную комбинацию соперника
            int diagonal_2_UserWeight = 0;
            for (int i = 0; i < 3; i++) {
                if (map[i][2 - i] == USER_DOT) {
                    diagonal_2_UserWeight++;
                    if (diagonal_2_UserWeight == 2) {
                        for (int j = 0; j < 3; j++) {
                            if (map[j][2 - j] == DOT_EMPTY) {
                                turnCoords[0] = j;
                                turnCoords[1] = 2 - j;
                                isTurnDone = 1;
                                break;
                            }
                        }
                    }
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;

            // ходы в углы
            if (map[0][0] == DOT_EMPTY) {
                turnCoords[0] = 0;
                turnCoords[1] = 0;
                isTurnDone = 1;
            } else if (map[2][2] == DOT_EMPTY) {
                turnCoords[0] = 2;
                turnCoords[1] = 2;
                isTurnDone = 1;
            } else if (map[0][2] == DOT_EMPTY) {
                turnCoords[0] = 0;
                turnCoords[1] = 2;
                isTurnDone = 1;
            } else if (map[2][0] == DOT_EMPTY) {
                turnCoords[0] = 2;
                turnCoords[1] = 0;
                isTurnDone = 1;
            }
            if (isTurnDone == 1) break;

            // если центр и углы заняты, и нет победного хода, ход делается в первую свободную клетку (слева-направо, сверху-вниз)
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (map[i][j] == DOT_EMPTY) {
                        turnCoords[0] = i;
                        turnCoords[1] = j;
                        isTurnDone = 1;
                        break;
                    }
                }
                if (isTurnDone == 1) break;
            }
            if (isTurnDone == 1) break;
        }
        return turnCoords;
    }
}

