package com.itsol.mockup.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import static com.itsol.mockup.utils.Constants.workingHoursPerMonth;

public class GeneticAlgorithm {
    private static final Random random = new Random();
    protected static final Logger logger = LoggerFactory.getLogger("GeneticAlgorithm");

//    static Map<int[], Double> fitnessCache = new HashMap<>();
//    static double fitnessForSingleUserWithCache(int[] individual, int[] taskDurations, int currentWorkloads, int[] taskLevel, int userLevel, int[] taskRisk, RiskFromUserEntity risk) {
//        return fitnessCache.computeIfAbsent(individual, ind ->
//                fitness(ind, taskDurations, currentWorkloads, taskLevel, userLevel, taskRisk, risk));
//    }

    // Khởi tạo quần thể
    public static List<int[]> initializePopulation(int populationSize, int numTasks, int numMembers) {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
//            logger.info("population {}", i);
            int[] individual = new int[numTasks];
            for (int j = 0; j < numTasks; j++)
            {
                individual[j] = random.nextInt(numMembers);
//                logger.info("{} {}", j, individual[j]);
            }
            population.add(individual);
        }
        return population;
    }

    public static double riskOfSingleTask(int individual, double taskRisk){
        //rủi ro của task
        double taskRiskMultiplier = individual * taskRisk;
        return (double) Math.round((taskRiskMultiplier * 50) * 100) /100 ;
    }

    // lấy giá trị level của task và user đem ra ss
    // result individual[i]: task i is assigned to member with idx num individual[i]
    // Đánh giá độ phù hợp của từng cá thể
    public static double fitness(int[] individual, int[] taskDurations, int currentWorkloads,
                                 int[] taskLevel, int userLevel, double[] taskRisk, double riskFromUserMultiplier) {

        double workinghours = workingHoursPerMonth;
        //rủi ro từ phía người dùng
        workinghours *= riskFromUserMultiplier;
//        int levelCount=0;
//        double taskRiskMultiplier=0;
        int workloads = currentWorkloads;
        double risk = 0;
        for (int i = 0; i < individual.length; i++) {
            //workload
            workloads += individual[i] * taskDurations[i];
            int levelCount = userLevel - taskLevel[i];
            risk += riskOfSingleTask(individual[i], taskRisk[i]) - (levelCount * individual[i] * 50);

        }
        return Math.abs(workloads - workinghours) * 200 + risk;
    }


    // Selection
    public static List<int[]> selection(List<int[]> population, double[] fitnessValues, int numSelect) {
        List<int[]> selected = new ArrayList<>();
        for (int i = 0; i < numSelect; i++) {
            int a = random.nextInt(population.size());
            int b = random.nextInt(population.size());
            //Get one with lower fitnessForTeam value
            if (fitnessValues[a] < fitnessValues[b]) {
                selected.add(population.get(a));
            } else {
                selected.add(population.get(b));
            }
        }
        return selected;
    }

    // Lai ghép một điểm
    public static int[][] crossover(int[] parent1, int[] parent2) {
        int crossoverPoint = random.nextInt(parent1.length);
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent2.length];

        System.arraycopy(parent1, 0, child1, 0, crossoverPoint);
        System.arraycopy(parent2, crossoverPoint, child1, crossoverPoint, parent1.length - crossoverPoint);

        System.arraycopy(parent2, 0, child2, 0, crossoverPoint);
        System.arraycopy(parent1, crossoverPoint, child2, crossoverPoint, parent2.length - crossoverPoint);

        return new int[][]{child1, child2};
    }

    // Đột biến ngẫu nhiên
    public static void mutation(int[] individual, int numMembers, double mutationRate) {
//        for (int i = 0; i < individual.length; i++) {
//            if (random.nextDouble() < mutationRate) {
//                individual[i] = random.nextInt(numMembers);
//            }
//        }
        if (random.nextDouble() < mutationRate*10){
            individual[random.nextInt(individual.length)] = random.nextInt(numMembers);
        }

    }

    // Vòng lặp chính của thuật toán di truyền
    public static int[] geneticAlgorithmSingleUser(int numTasks,
                                                   int numGenerations, int populationSize,
                                                   int currentWorkloads, int[] taskDurations,
                                                   int[] taskLevel, int userLevel, double[] taskRisk, double riskFromUser)
    {
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        int[] bestIndividual = null;
        double bestFitness = Double.MAX_VALUE;
        List<int[]> population = initializePopulation(populationSize, numTasks,2);
        logger.info("Loop through {} gens", numGenerations);
        for (int generation = 0; generation < numGenerations; generation++) {

            double[] fitnessValues;
            List<int[]> selected;
//            logger.info("calculate fitnessForTeam");
//            for (int i = 0; i < population.size(); i++) {
////                fitnessValues[i] = fitnessForSingleUserWithCache(population.get(i), taskDurations, currentWorkloads, taskLevel, userLevel, taskRisk, risk);
//                fitnessValues[i] = fitness(population.get(i), taskDurations, currentWorkloads, taskLevel, userLevel, taskRisk, riskFromUser);
////                if(i%2==1){
////                    if (fitnessValues[i] < fitnessValues[i-1]) {
////                        selected.add(population.get(i));
////                    }else {
////                        selected.add(population.get(i-1));
////                    }
////                }
//            }
//            logger.info("select");
            // Tính toán fitnessValues bằng Stream
            fitnessValues = population.stream()
                    .mapToDouble(individual -> fitness(individual, taskDurations, currentWorkloads, taskLevel, userLevel, taskRisk, riskFromUser))
                    .toArray();

            selected = selection(population, fitnessValues, populationSize / 2);

//            logger.info("crossover");
            List<int[]> nextGeneration = new ArrayList<>();
            while (nextGeneration.size() < populationSize) {
                int[] parent1 = selected.get(random.nextInt(selected.size()));
                int[] parent2 = selected.get(random.nextInt(selected.size()));

                int[][] children = crossover(parent1, parent2);
                mutation(children[0], 2, 0.02);
                mutation(children[1], 2, 0.02);

                nextGeneration.add(children[0]);
                nextGeneration.add(children[1]);
            }
            logger.info("generation {}, sample individual {}", generation, population.get(0));
            population = nextGeneration;
        }
// Chọn cá thể tốt nhất
        logger.info("choose best individual");
        for (int[] individual : population) {
//            double currentFitness = fitnessForSingleUserWithCache(individual, taskDurations,  currentWorkloads, taskLevel, userLevel, taskRisk, risk);
            double currentFitness = fitness(individual, taskDurations,  currentWorkloads, taskLevel, userLevel, taskRisk, riskFromUser);
//            logger.info("current individual fitnessForTeam " + currentFitness + " " + Arrays.toString(individual) + " vs bestFitness " + bestFitness);
            if (currentFitness < bestFitness) {
//                logger.info("current lower ");
                bestFitness = currentFitness;
                bestIndividual = individual;
            }
        }
//        int[] test1 = {0, 0, 0, 0, 1, 1, 0, 1, 1};
//        logger.info(""+fitness(test1, taskDurations,  currentWorkloads, taskLevel, userLevel, taskRisk, riskFromUser));
//        int[] test2 = {0, 0, 0, 0, 0, 1, 1, 1, 1};
//        logger.info(""+fitness(test2, taskDurations,  currentWorkloads, taskLevel, userLevel, taskRisk, riskFromUser));
//        System.out.println("best individual fitnessForTeam " + bestFitness);
//        for(int i = 0;i<=bestIndividual.length;i++){
//            logger.info("Best individual " + i + " " + bestIndividual[i]);
//        }
        return bestIndividual;
    }

    public static int[] geneticAlgorithmForTeam(int numTasks, int numMembers,
                                                int numGenerations, int populationSize,
                                                int[] currentWorkloads, int[] taskDurations,
                                                int[] taskLevel, int[] userLevel,
                                                int[] taskRole, int[][] userRoles,
                                                double[] taskRisk, double[] riskFromUsers) {
//        List<int[]> population = initializePopulation(populationSize, numTasks, numMembers);
        List<int[]> population = initializePopulation(populationSize, numTasks, numMembers+1);
//        for(int[] individual : population2){
//            logger.info(Arrays.toString(individual));
//        }
        for (int generation = 0; generation < numGenerations; generation++) {
            logger.info("t_generation {}", generation);
            double[] fitnessValues = new double[population.size()];

            for (int i = 0; i < population.size(); i++) {
                fitnessValues[i] = fitnessForTeam(population.get(i), taskDurations, numMembers, currentWorkloads,
                        taskLevel, userLevel, taskRole, userRoles, taskRisk, riskFromUsers);
            }

//            fitnessValues = population.stream()
//                    .mapToDouble(individual -> fitnessForTeam(individual, taskDurations, numMembers, currentWorkloads,
//                    taskRole, userRoles, taskRisk, riskFromUsers))
//                    .toArray();

            List<int[]> selected = selection(population, fitnessValues, populationSize / 2);

            List<int[]> nextGeneration = new ArrayList<>();
            while (nextGeneration.size() < populationSize) {
                int[] parent1 = selected.get(random.nextInt(selected.size()));
                int[] parent2 = selected.get(random.nextInt(selected.size()));

                int[][] children = crossover(parent1, parent2);

                mutation(children[0], numMembers, 0.02);
                mutation(children[1], numMembers, 0.02);

                nextGeneration.add(children[0]);
                nextGeneration.add(children[1]);
            }
            population = nextGeneration;
        }

        // Chọn cá thể tốt nhất
        int[] bestIndividual = null;
        double bestFitness = Double.MAX_VALUE;
        for (int[] individual : population) {
            double currentFitness = fitnessForTeam(individual, taskDurations, numMembers, currentWorkloads,
                    taskLevel, userLevel, taskRole, userRoles, taskRisk, riskFromUsers);
            if (currentFitness < bestFitness) {

                bestFitness = currentFitness;
                bestIndividual = individual;
            }
        }
//        System.out.println("best individual fitnessForTeam " + bestFitness);
        return bestIndividual;
    }

    public static double fitnessForTeam(int[] individual, int[] taskDurations, int numMembers, int[] currentWorkloads,
                                        int[] taskLevel, int[] userLevel,
                                        int[] taskRole, int[][] userRoles, double[] taskRisk, double[] riskFromUserMultiplier) {
        // Initialize workloads with current workloads
        double[] workingHoursForUsers = new double[numMembers];
        for(int i = 0; i< workingHoursForUsers.length; i++){
            workingHoursForUsers[i] = (double) workingHoursPerMonth *riskFromUserMultiplier[i];
        }

        int[] membersWorkloads = new int[numMembers];
        System.arraycopy(currentWorkloads, 0, membersWorkloads, 0, numMembers);

        int roleCount = 0;
        boolean rightRole;
//        double[] risk = new double[numMembers];
        double risk = 0;
        int taskAssigned;
        int taskNotAssignedCount = 0;
        // Calculate workloads based on the individual assignment
        for (int i = 0; i < individual.length; i++) {
            rightRole = false;
            if(individual[i] == 3) continue;
            membersWorkloads[individual[i]] += taskDurations[i];
            if(individual[i] != numMembers) taskAssigned = 1;
            else {
                taskAssigned = 0;
                taskNotAssignedCount++;
            }
//            risk[individual[i]] += riskOfSingleTask(taskAssigned, taskLevel[i], userLevel[individual[i]], taskRisk[i]);
            risk += riskOfSingleTask(taskAssigned, taskRisk[i]);

            if(individual[i] != numMembers) risk -= 50 * individual[i] * (userLevel[individual[i]] - taskLevel[i]);

            for(int k = 0; k < userRoles[individual[i]].length;k++){
                if (userRoles[individual[i]][k] == taskRole[i]){
                    roleCount++;
                    rightRole = true;
                    break;
                }
            }
            if(!rightRole) roleCount = Integer.MIN_VALUE;
        }
        double[] workingHoursDiffOfUser = new double[numMembers];
        double workingHoursDiff = 0;
        for(int i = 0; i < workingHoursForUsers.length; i++){
            workingHoursDiffOfUser[i] = Math.abs(workingHoursForUsers[i]-membersWorkloads[i]);
            workingHoursDiff +=workingHoursDiffOfUser[i];
        }
        // Return the fitnessForTeam value
        return workingHoursDiff*100 - roleCount*200 + risk*0.75 + taskNotAssignedCount*100;
    }

}
