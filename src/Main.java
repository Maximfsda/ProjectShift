import java.io.*;
import java.util.*;

public class MergeSortFiles {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java MergeSortFiles <sort_mode> <data_type> <output_file> <input_files...>");
            return;
        }

        String sortMode = args[0];
        String dataType = args[1];
        String outputFile = args[2];
        String[] inputFiles = Arrays.copyOfRange(args, 3, args.length);

        try {
            List<String> lines = new ArrayList<>();

            for (String inputFile : inputFiles) {
                List<String> fileLines = readLinesFromFile(inputFile);
                lines.addAll(fileLines);
            }

            if (dataType.equals("-i")) {
                List<Integer> numbers = parseIntegerList(lines);
                mergeSort(numbers, sortMode);
                writeNumbersToFile(outputFile, numbers);
            } else if (dataType.equals("-s")) {
                mergeSort(lines, sortMode);
                writeLinesToFile(outputFile, lines);
            } else {
                System.out.println("Invalid data type. Use -i for integers or -s for strings.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading/writing the files: " + e.getMessage());
        }
    }

    private static List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        reader.close();
        return lines;
    }

    private static List<Integer> parseIntegerList(List<String> lines) {
        List<Integer> numbers = new ArrayList<>();

        for (String line : lines) {
            numbers.add(Integer.parseInt(line));
        }

        return numbers;
    }

    private static void mergeSort(List<?> list, String sortMode) {
        if (list.size() <= 1) {
            return;
        }

        int mid = list.size() / 2;
        List<?> left = list.subList(0, mid);
        List<?> right = list.subList(mid, list.size());

        mergeSort(left, sortMode);
        mergeSort(right, sortMode);

        merge(list, left, right, sortMode);
    }

    private static void merge(List<Object> result, List<?> left, List<?> right, String sortMode) {
        int i = 0, j = 0, k = 0;
        Comparator<Object> comparator = (sortMode.equals("-d")) ? Collections.reverseOrder() : null;

        while (i < left.size() && j < right.size()) {
            Object leftElem = left.get(i);
            Object rightElem = right.get(j);

            if (comparator != null) {
                if (comparator.compare(leftElem, rightElem) > 0) {
                    result.set(k++, leftElem);
                    i++;
                } else {
                    result.set(k++, rightElem);
                    j++;
                }
            } else {
                if (((Comparable<Object>) leftElem).compareTo(rightElem) <= 0) {
                    result.set(k++, leftElem);
                    i++;
                } else {
                    result.set(k++, rightElem);
                    j++;
                }
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }

    private static void writeLinesToFile(String fileName, List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

    private static void writeNumbersToFile(String fileName, List<Integer> numbers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (Integer number : numbers) {
            writer.write(number.toString());
            writer.newLine();
        }

        writer.close();
    }
}
