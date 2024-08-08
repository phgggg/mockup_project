package com.itsol.mockup.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private static final Random random = new Random();

    // Khởi tạo quần thể
    public static List<int[]> initializePopulation(int populationSize, int numTasks, int numMembers) {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
//            System.out.println("population " + i);
            int[] individual = new int[numTasks];
            for (int j = 0; j < numTasks; j++) {
                individual[j] = random.nextInt(numMembers);
//                System.out.println( j +  " " + individual[j]);
            }
            population.add(individual);
        }
        return population;
    }

    // lấy giá trị level của task và user đem ra ss
    //result individual[i]: task i is assigned to member with idx num individual[i]
    // Đánh giá độ phù hợp của từng cá thể
    public static double fitness(int[] individual, int[] taskDurations, int numMembers, int[] currentWorkloads, int[] taskLevel, int[] userLevel) {
        // Initialize workloads with current workloads
        int[] workloads = new int[numMembers];
        System.arraycopy(currentWorkloads, 0, workloads, 0, numMembers);
        int cnt =0;
        // Calculate workloads based on the individual assignment
        for (int i = 0; i < individual.length; i++) {
            workloads[individual[i]] += taskDurations[i];
            if(taskLevel[i] <= userLevel[individual[i]]) {
                cnt+=1;
            }
        }

        // Calculate the maximum workload
        int maxWorkload = 0;
        for (int workload : workloads) {
            if (workload > maxWorkload) {
                maxWorkload = workload;
            }
        }

        // Calculate the average workload
        double averageWorkload = 0;
        for (int workload : workloads) {
            averageWorkload += workload;
        }
        averageWorkload /= numMembers;

        // Calculate the balance score
        double balanceScore = 0;
        for (int workload : workloads) {
            balanceScore += Math.pow(workload - averageWorkload, 2);
        }
        balanceScore = Math.sqrt(balanceScore);
        // Return the fitness value
        return maxWorkload + balanceScore -cnt*100;
    }


    // Chọn lọc giải đấu
    public static List<int[]> selection(List<int[]> population, double[] fitnessValues, int numSelect) {
        List<int[]> selected = new ArrayList<>();
        for (int i = 0; i < numSelect; i++) {
            int a = random.nextInt(population.size());
            int b = random.nextInt(population.size());
            //Get one with lower fitness value
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
        for (int i = 0; i < individual.length; i++) {
            if (random.nextDouble() < mutationRate) {
                individual[i] = random.nextInt(numMembers);
            }
        }
    }

    // Vòng lặp chính của thuật toán di truyền
    public static int[] geneticAlgorithm(int numTasks, int numMembers,
                                         int numGenerations, int populationSize,
                                         int[] currentWorkloads,
                                         int[] taskDurations, int[] taskLevel, int[] userLevel) {
        List<int[]> population = initializePopulation(populationSize, numTasks, numMembers);

        for (int generation = 0; generation < numGenerations; generation++) {
//            System.out.println("generation " + generation);
            double[] fitnessValues = new double[population.size()];
            for (int i = 0; i < population.size(); i++) {
                String pop = "";
                for(int k = 0;k< population.get(i).length;k++){
                    pop+=population.get(i)[k]+" ";
                }
//                System.out.println("population " + i + " " +pop);
                fitnessValues[i] = fitness(population.get(i), taskDurations, numMembers, currentWorkloads, taskLevel, userLevel);
//                System.out.println("fitness value of population " + i + " " + fitnessValues[i]);
            }
//            System.out.println("Chon 25");
            List<int[]> selected = selection(population, fitnessValues, populationSize / 2);

            List<int[]> nextGeneration = new ArrayList<>();
            while (nextGeneration.size() < populationSize) {
                int[] parent1 = selected.get(random.nextInt(selected.size()));
                int[] parent2 = selected.get(random.nextInt(selected.size()));

                int[][] children = crossover(parent1, parent2);

                mutation(children[0], numMembers, 0.01);
                mutation(children[1], numMembers, 0.01);

                nextGeneration.add(children[0]);
                nextGeneration.add(children[1]);
            }
//            int i =1;
//            for(int[] a : nextGeneration){
//
//                System.out.print("p " +i + " ");
//                for(int z = 0;z< a.length;z++){
//                    System.out.print(a[z] + " ");
//                }
//                System.out.println("");
//                i++;
//            }
            population = nextGeneration;
        }

        // Chọn cá thể tốt nhất
        int[] bestIndividual = null;
        double bestFitness = Double.MAX_VALUE;
        for (int[] individual : population) {
            double currentFitness = fitness(individual, taskDurations, numMembers, currentWorkloads, taskLevel, userLevel);
            System.out.println("current individual fitness " + currentFitness);
            if (currentFitness < bestFitness) {
                bestFitness = currentFitness;
                bestIndividual = individual;
            }
        }
        System.out.println("best individual fitness " + bestFitness);
        return bestIndividual;
    }

    public static void main(String[] args) {
//        int numMembers = 3;
////        int numTasks = 18;
////        int[] taskDurations = {9, 12, 12, 15, 18, 18, 18, 21, 21, 21, 21, 24, 24, 24, 27, 30, 30, 33};
//        int numTasks = 06;
//        int[] taskDurations = {30, 12, 9, 33, 12, 30};
//        int numGenerations = 100;
//        int populationSize = 50;
//
//        //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
//        int[] bestAssignment = geneticAlgorithm(numTasks, numMembers, taskDurations, numGenerations, populationSize);
//
//        int mem1=0,mem2=0,mem3=0;//tasks per member
//        int mem1c=0,mem2c=0,mem3c=0;//total workload
//        System.out.println("Best assignment:");
//        for (int i = 0; i < bestAssignment.length; i++) {
//            System.out.printf("Task %d assigned to Member %d\n", i, bestAssignment[i]);
//            switch (bestAssignment[i]){
//                case 0:
//                    mem1++;mem1c+=taskDurations[i];break;
//                case 1:
//                    mem2++;mem2c+=taskDurations[i];break;
//                default:
//                    mem3++;mem3c+=taskDurations[i];break;
//            }
//        }
//
//        System.out.println("tasks per member: " + mem1+" " + mem2 + " " + mem3);
//        System.out.println("total workload: " + mem1c+" " + mem2c + " " + mem3c);
    }
}
