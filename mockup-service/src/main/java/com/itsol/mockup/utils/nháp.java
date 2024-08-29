//import static com.itsol.mockup.utils.Constants.workingHoursPerMonth;
//import static com.itsol.mockup.utils.GeneticAlgorithm.riskOfSingleTask;
//
//public static double fitness(int[] individual, int[] taskDurations, int currentWorkloads,
//                             int[] taskLevel, int userLevel, double[] taskRisk, double riskFromUserMultiplier) {
//        double workinghours = workingHoursPerMonth;
//        //rủi ro từ phía người dùng
//        workinghours *= riskFromUserMultiplier;
//    int workloads = currentWorkloads;
//        double risk = 0;
//        for (int i = 0; i < individual.length; i++) {
//            //workload
//            workloads += individual[i] * taskDurations[i];
//            risk += riskOfSingleTask(individual[i], taskLevel[i], userLevel, taskRisk[i]);
//        }
//        return Math.abs(workloads - workinghours) * 200 + risk;
//    }