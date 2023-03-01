// Written by Angel Alvarez for CIT-63

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
// You will use functions to calculate the following data elements for each animal:
// Unique animal ID, animal name, birth date, color, weight, origin

public class CppZoo {
    public static void main(String[] args) {
        String [][] animals = getAnimalData();
        String [][][] animalsByHabitat = genZooHabitats(animals);
        String[] habitatNames = genHabitatNames(animals);
        genUanimalID(animals);
        genBirthDay(animals);
        genArrivalDate(animals);
        genAnimalName(animals);
        writeToFile(animalsByHabitat, habitatNames);
        
        // THESE ARE USED FOR DEBUGGING PURPOSES HEHE
        // for (String[] array : animals) {
        //     System.out.println("");
        //     for (String word: array) {
        //         System.out.println(word);
        //     }
        //     System.out.println("");
        // }
    }

    // add animals to new file, order them by species (habitat)
    public static void writeToFile(String[][][] animalsByHabitat, String[] habitats) {
        // create file
        try {
            File file = new File("zooPopulation.txt");
            if (file.createNewFile()) {
                System.out.println("File created is: " + file.getName());
            }
            else {
                System.out.println("File already exists! ");
            }
        } catch (IOException e) {
            System.out.println("Exception caught! ");
            e.printStackTrace();
        }

        // write to file
        try {
            FileWriter writer = new FileWriter("zooPopulation.txt");
            writer.write("Midterm Program Output; by Angel Alvarez, Spring 2023, Fresno, CA\n\n\n");
            
            for (int i = 0; i < 4; i++) {
                writer.write(habitats[i].substring(0, 1).toUpperCase() + habitats[i].substring(1) + " habitat:\n\n");
                for (int j = 0; j < 4; j++) {
                    // this is how every single one of the subarrays in animals looks like
                    // [id, name, age, birth date, birth month, color, sex, weight, origin, arrival date, species]
                    //   0   1      2      3             4         5     6     7        8          9         10 
                    // this is how the output should look like
                    // id; name; age; birth date; color; sex; weight; origin; arrival date
                    String lineToWrite = animalsByHabitat[i][j][0] + "; " + animalsByHabitat[i][j][1] + "; " + animalsByHabitat[i][j][2] + "; ";
                    lineToWrite += animalsByHabitat[i][j][3] + "; " + animalsByHabitat[i][j][5] + "; " + animalsByHabitat[i][j][6] + "; ";
                    lineToWrite += animalsByHabitat[i][j][7] + "; " + animalsByHabitat[i][j][8] + "; " + animalsByHabitat[i][j][9] + "\n";
                    writer.write(lineToWrite);
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("File write complete.");
        } catch (IOException e ){
            System.out.println("File IO exception caught!");
            e.printStackTrace();
        }
    }


    public static void genAnimalName(String[][] animals) {
        for (String[] animal : animals) {
            try {
                File file = new File("animalNames.txt");
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] wordsInLine = line.split(" ");
                    if ( wordsInLine[0].toLowerCase().equals(animal[10])) {
                        // go to next line and get animal names
                        line = scanner.nextLine();
                        String[] animalNames = line.split(", ");

                        // choose a random name from animalNames and assign it to current animal
                        int randomIndex = new Random().nextInt(animalNames.length); 
                        animal[1] = animalNames[randomIndex];
                        break; // exit the loop and find name for next animal
                    }
                } 
                scanner.close();
            } catch (IOException e) {
                System.out.println("File IO exception caught!");
                e.printStackTrace();
            }
        }


    }

    public static void genBirthDay(String[][] animals) {
        for (String[] animal : animals) {
            // 2023 - age = year born
            int year = 2023 - Integer.parseInt(animal[2].split(" ")[0]);
            int randomDay = new Random().nextInt(30) + 1;
            animal[3] = "birth date " + animal[4] + " " + randomDay + ", " + year; 
        }
    }

    public static void genUanimalID(String[][] animals) {
        for (int i = 0; i < 16; i++) {
            animals[i][0] = animals[i][10].substring(0, 2) + "0" + ((i % 4) + 1);
        }
    }

    public static String[] genHabitatNames(String[][] animals) {
        String[] habitats = new String[4];
        int index = 0;
        boolean alreadyInHabitats = false;
        for (String[] animal : animals) {
            alreadyInHabitats = false;
            for (String species : habitats) {
                if (animal[10].equals(species)) {
                    alreadyInHabitats = true;
                    
                }
            }

            if (!alreadyInHabitats && index < 4) {
                habitats[index] = animal[10];
                index++;
            }
        }
        return habitats;
    }

    public static String[][][] genZooHabitats(String[][] animals) {
        String[][][] animalsByHabitat = new String[4][4][1];
        int animalIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                animalsByHabitat[i][j] = animals[animalIndex];
                animalIndex++;
            }
        }
        return animalsByHabitat;
    }

    public static void genArrivalDate(String[][] animals) {
        for (String[] animal : animals) {
            int age = 2023 - Integer.parseInt(animal[2].split(" ")[0]);
            // Generate a random year between (year born) + 1 and 2023
            int arrivalYear = new Random().nextInt(2023 - age) + age;
            int randomDay = new Random().nextInt(30) + 1;
            animal[9] = "arrived " + animal[4] + " " + randomDay + ", " + arrivalYear; 
        }
    }
    
    public static String getRandomMonth(String[] array) {
        int rand = new Random().nextInt(array.length);
        return array[rand];
    }

    public static String[][] getAnimalData() {
        // each element in animalDataMatrix will have the following structure:
        // [id, name, age, birth date, birth season, color, sex, weight, origin, arrival date]
        String[][] animalDataMatrix = new String[16][11];
        String[] animalDataArray = new String[11];
        String[] dataArray = new String[6];
        String[] sectionDataArray = new String[5];
        String age, sex, species, birthSeason, color, weight, origin;
        // these will be used for randomly choosing a birthDate and arrivalDate
        String[] springMonths = {"March", "April", "May"};
        String[] summerMonths = {"June", "July", "August"};
        String[] fallMonths = {"September", "October", "November"};
        String[] winterMonths = {"December", "January", "February"};
        int index = 0;

        try {
            File file = new File("arrivingAnimals.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine() && index < 16) {
                String line = scanner.nextLine();
                // handle edge case where an empty line is read
                if (line.equals("")) continue;
                // here we store the values extracted from arrivingAnimals.txt, which are separated by a comma
                dataArray = line.split(", ");
                // dataArray will be as follows
                // [age sex and species, born on, color, weight, origin(city/place), country of origin]
                // some of the values in data array should be separated again, such as the first one which
                // contains three values: age, sex, and species, for that we use the sectionDataArray array
                sectionDataArray = dataArray[0].split(" ");
                // sectionDataArray will be as follows
                // [age, "year", "old", sex, species]
                age = sectionDataArray[0] + " years old";
                sex = sectionDataArray[3];
                species = sectionDataArray[4];
                birthSeason = dataArray[1];
                color = dataArray[2];
                weight = dataArray[3];
                origin = dataArray[4] + ", " + dataArray[5];
                // [id, name, age, birth date, birth month, color, sex, weight, origin, arrival date, species]
                //   0   1      2      3             4         5     6     7        8          9         10 
                animalDataArray[0] = ""; // all empty values, will be set using another function
                animalDataArray[1] = "";
                animalDataArray[2] = age;
                animalDataArray[3] = "";
                // set random birth month based on season born
                if (birthSeason.contains("summer")) {
                    animalDataArray[4] = getRandomMonth(summerMonths);
                } else if (birthSeason.contains("fall")) {
                    animalDataArray[4] = getRandomMonth(fallMonths);
                } else if (birthSeason.contains("spring")) {
                    animalDataArray[4] = getRandomMonth(springMonths);
                } else if (birthSeason.contains("winter")) {
                    animalDataArray[4] = getRandomMonth(winterMonths);
                } else {
                    animalDataArray[4] = "unknown";
                }
                animalDataArray[5] = color;
                animalDataArray[6] = sex;
                animalDataArray[7] = weight;
                animalDataArray[8] = origin;
                animalDataArray[9] = "";
                animalDataArray[10] = species;
                // We use clone() because we want to create a copy of animalDataArray, otherwise
                // we would end up with an array full of the same animal's data multiple times
                animalDataMatrix[index] = animalDataArray.clone(); // adds one animal's data to our ArrayList of animals
                index++;
            }
            scanner.close();
        } catch (IOException e ){
            System.out.println("File IO exception caught!");
            e.printStackTrace();
        }


        return animalDataMatrix;
    }
}
