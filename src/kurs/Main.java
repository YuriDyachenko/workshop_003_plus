package kurs;

//Сегодня задача №2 не связана с первой задачей. Сегодня мы напишем функцию, которая снова на вход
//получает текст и ищет в нем слова паразиты. Словами-паразитами называются слова, которые встречаются
//в тексте >= N раз, где N - целочисленный параметр, который передается вторым параметром в функцию.
//Несколько правил:
//- Словом считается любой набор символов, обособленный слева и справа пробелами ИЛИ началом/концом строки.
//- Слова с разным регистром считаются одним и тем же словом. То есть предлог "под", который мы можем встретить
// в середине предложения и "Под" - в начале предложения - одно и то же слово.
//- Знаки препинания не учитываются. То есть "привет." и "привет" - это одно и то же слово.
//Гарантируется, что текст будет только на русском или английском языках.
//Результат вернуть JSON'ом, где ключи - слова-паразиты, а значение - количество раз, которое оно встречается.
//Пример:
//Дано:
//text: "Ну что ж я, я найти решения правильного не смогу ж? Смогу ж конечно, я ж старательный все ж таки."
//max_amount: 3
//Ответ:
//{
//"я":3,
//"ж":5
//}

public class Main {

    public static void main(String[] args) {
	    String inText = "Ну что ж я, я  найти   решения правильного не смогу ж? " +
                "Смогу ж конечно, я ж старательный все ж таки.";
        System.out.println(analysis(inText, 3));
    }

    private static String analysis(String text, int max_amount) {
        //очень интересная задача, снова нужно где-то динамически накапливать слова
        //причем переводить в нижний регистр и как-то убирать в нем лишние символы
        //но никто не знает, какие там могут встретиться, написано только про русские и английские буквы
        //можно убрать все символы, кроме правильных
        String inText = text.toLowerCase();
        StringBuilder inBuilder = new StringBuilder();
        String legalChars = " abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        for (int i = 0; i < inText.length(); i++) {
            char c = inText.charAt(i);
            if (legalChars.indexOf(c) == -1) continue;
            //убираем случаи, когда вместо пробела одного пришло несколько
            //иначе у нас будут после split слова пустые в массиве еще
            if (c == ' ' && inBuilder.length() > 0 && inBuilder.charAt(inBuilder.length() - 1) == ' ') continue;
            inBuilder.append(c);
        }
        String[] wordArray = inBuilder.toString().split(" ");
        //теперь задачка тоже сложная, есть массив слов, некоторые встречаются несколько раз
        //нам нужно число уникальных слов, чтобы создать выходной массив
        //а как его узнать? никак. снова нужны динамические списки какие-то
        //но у нас их нет, будем создавать с запасом
        //в худшем случае у нас будет все слова по одному разу
        String[] tempArray = new String[wordArray.length];
        int[] tempCounters = new int[wordArray.length];
        int totalSize = 0;
        for (int i = 0; i < wordArray.length; i++) {
            //вот у нас есть слово
            //нужно найти его в массиве для начала
            //если нету, поместить на свободное место
            //а где же считать количество? нельзя же во второе измерение поместить
            //придется сделать второй массив счетчиков
            int found = -1;
            for (int j = 0; j < totalSize; j++) {
                if (tempArray[j] != null && tempArray[j].equals(wordArray[i])) {
                    found = j;
                    break;
                }
            }
            if (found == -1) found = totalSize;
            tempArray[found] = wordArray[i];
            tempCounters[found]++; 
            totalSize++;
        }
        StringBuilder outBuilder = new StringBuilder();
        for (int i = 0; i < totalSize; i++) {
            if (tempCounters[i] < max_amount) continue;
            if (outBuilder.length() != 0) {
                //тут что-то уже есть, значит, нужно добавить запятую и перевод строки
                outBuilder.append(",\n");
            }
            outBuilder.append(String.format("\t\"%s\":%d", tempArray[i], tempCounters[i]));
        }
        return "{\n" + outBuilder.toString() + "\n}";
    }
}
