import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("ERROR: please provide the path for log file and tag mapping");
            System.out.println("Input format should be 'java Main <log-file> <tag-file>'");
            System.out.println("For example:");
            System.out.println("java Main ./test/log ./test/lookup.csv");
            return;
        }

        String logPath = args[0];
        String tagPath = args[1];

        Map<Integer, String> codeToProtocolMap = getProtocolMapping();
        Map<String, String> tagMapping = getTagMapping(tagPath);

        Map<String, Integer> tagCount = new HashMap<>();
        Map<String, Integer> comCount = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] values = line.trim().split(" ");
                    String dstPort = values[6];
                    String protocolCode = values[7];
                    String protocol = codeToProtocolMap.get(Integer.parseInt(protocolCode));
                    String comb = dstPort + ',' + protocol;
                    String tag = tagMapping.getOrDefault(comb, "Untagged");
                    tagCount.put(tag, tagCount.getOrDefault(tag, 0) + 1);
                    comCount.put(comb, comCount.getOrDefault(comb, 0) + 1);
                }
            }

        } catch (IOException e) {
            System.out.println("Error find log file, please provide valid log file path!");
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../output/result.txt"))) {
            writer.write("Tag Counts:");
            writer.newLine();
            writer.write("Tag,Count ");
            writer.newLine();
            for (String tag : tagCount.keySet()) {
                writer.write(tag + ',' + tagCount.get(tag));
                writer.newLine();
            }
            writer.newLine();
            writer.write("------------------------");
            writer.newLine();
            writer.newLine();
            writer.write("Port/Protocol Combination Counts:");
            writer.newLine();
            writer.write("Port,Protocol,Count ");
            writer.newLine();
            for (String comb : comCount.keySet()) {
                writer.write(comb + ',' + comCount.get(comb));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Finished processing all logs, please check file under output folder");
    }

    private static Map<Integer, String> getProtocolMapping() {
        Map<Integer, String> codeToProtocolMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("protocol-numbers-1.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].chars().allMatch(Character::isDigit)) {
                    codeToProtocolMap.put(Integer.parseInt(values[0]), values[1].toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Error find protocol number mapping, make sure this file is not removed!");
            throw new RuntimeException(e);
        }
        return codeToProtocolMap;
    }

    private static Map<String, String> getTagMapping(String filePath) {
        Map<String, String> tagMapping = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) >= '0' && line.charAt(0) <= '9') {
                    String[] values = line.split(",");
                    tagMapping.put(values[0] + ',' + values[1], values[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error find tag mapping, please provide valid tag mapping file path!");
            throw new RuntimeException(e);
        }
        return tagMapping;
    }
}