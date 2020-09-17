package com.example.multidatasource.demo.service;

public class SqoopClient {

    public void init() {
        String url = "http://localhost:12000/sqoop/";
        SqoopClient client = new SqoopClient(url);
//        client.setServerUrl(newUrl);
    }

    public void saveLink() {
        // create a placeholder for link
        long connectorId = 1;
        MLink link = client.createLink(connectorId);
        link.setName("Vampire");
        link.setCreationUser("Buffy");
        MLinkConfig linkConfig = link.getConnectorLinkConfig();
// fill in the link config values
        linkConfig.getStringInput("linkConfig.connectionString").setValue("jdbc:mysql://localhost/my");
        linkConfig.getStringInput("linkConfig.jdbcDriver").setValue("com.mysql.jdbc.Driver");
        linkConfig.getStringInput("linkConfig.username").setValue("root");
        linkConfig.getStringInput("linkConfig.password").setValue("root");
// save the link object that was filled
        Status status = client.saveLink(link);
        if(status.canProceed()) {
            System.out.println("Created Link with Link Id : " + link.getPersistenceId());
        } else {
            System.out.println("Something went wrong creating the link");
        }
    }

    public void saveJob() {
        String url = "http://localhost:12000/sqoop/";
        SqoopClient client = new SqoopClient(url);
//Creating dummy job object
        long fromLinkId = 1;// for jdbc connector
        long toLinkId = 2; // for HDFS connector
        MJob job = client.createJob(fromLinkId, toLinkId);
        job.setName("Vampire");
        job.setCreationUser("Buffy");
// set the "FROM" link job config values
        MFromConfig fromJobConfig = job.getFromJobConfig();
        fromJobConfig.getStringInput("fromJobConfig.schemaName").setValue("sqoop");
        fromJobConfig.getStringInput("fromJobConfig.tableName").setValue("sqoop");
        fromJobConfig.getStringInput("fromJobConfig.partitionColumn").setValue("id");
// set the "TO" link job config values
        MToConfig toJobConfig = job.getToJobConfig();
        toJobConfig.getStringInput("toJobConfig.outputDirectory").setValue("/usr/tmp");
// set the driver config values
        MDriverConfig driverConfig = job.getDriverConfig();
        driverConfig.getStringInput("throttlingConfig.numExtractors").setValue("3");

        Status status = client.saveJob(job);
        if(status.canProceed()) {
            System.out.println("Created Job with Job Id: "+ job.getPersistenceId());
        } else {
            System.out.println("Something went wrong creating the job");
        }
    }

    public void viewError() {
        printMessage(link.getConnectorLinkConfig().getConfigs());
    }

    private static void printMessage(List<MConfig> configs) {
        for (MConfig config : configs) {
            List<MInput<?>> inputlist = config.getInputs();
            if (config.getValidationMessages() != null) {
                // print every validation message
                for (Message message : config.getValidationMessages()) {
                    System.out.println("Config validation message: " + message.getMessage());
                }
            }
            for (MInput minput : inputlist) {
                if (minput.getValidationStatus() == Status.WARNING) {
                    for (Message message : config.getValidationMessages()) {
                        System.out.println("Config Input Validation Warning: " + message.getMessage());
                    }
                } else if (minput.getValidationStatus() == Status.ERROR) {
                    for (Message message : config.getValidationMessages()) {
                        System.out.println("Config Input Validation Error: " + message.getMessage());
                    }
                }
            }
        }
    }

    public void jobStart() {
        //Job start
        long jobId = 1;
        MSubmission submission = client.startJob(jobId);
        System.out.println("Job Submission Status : " + submission.getStatus());
        if(submission.getStatus().isRunning() && submission.getProgress() != -1) {
            System.out.println("Progress : " + String.format("%.2f %%", submission.getProgress() * 100));
        }
        System.out.println("Hadoop job id :" + submission.getExternalId());
        System.out.println("Job link : " + submission.getExternalLink());
        Counters counters = submission.getCounters();
        if(counters != null) {
            System.out.println("Counters:");
            for(CounterGroup group : counters) {
                System.out.print("\t");
                System.out.println(group.getName());
                for(Counter counter : group) {
                    System.out.print("\t\t");
                    System.out.print(counter.getName());
                    System.out.print(": ");
                    System.out.println(counter.getValue());
                }
            }
        }
        if(submission.getExceptionInfo() != null) {
            System.out.println("Exception info : " +submission.getExceptionInfo());
        }


//Check job status for a running job
        MSubmission submission = client.getJobStatus(jobId);
        if(submission.getStatus().isRunning() && submission.getProgress() != -1) {
            System.out.println("Progress : " + String.format("%.2f %%", submission.getProgress() * 100));
        }

//Stop a running job
        submission.stopJob(jobId);
    }

    public void display() {
        String url = "http://localhost:12000/sqoop/";
        SqoopClient client = new SqoopClient(url);
        long connectorId = 1;
// link config for connector
        describe(client.getConnector(connectorId).getLinkConfig().getConfigs(), client.getConnectorConfigBundle(connectorId));
// from job config for connector
        describe(client.getConnector(connectorId).getFromConfig().getConfigs(), client.getConnectorConfigBundle(connectorId));
// to job config for the connector
        describe(client.getConnector(connectorId).getToConfig().getConfigs(), client.getConnectorConfigBundle(connectorId));

        void describe(List<MConfig> configs, ResourceBundle resource) {
            for (MConfig config : configs) {
                System.out.println(resource.getString(config.getLabelKey())+":");
                List<MInput<?>> inputs = config.getInputs();
                for (MInput input : inputs) {
                    System.out.println(resource.getString(input.getLabelKey()) + " : " + input.getValue());
                }
                System.out.println();
            }
        }
    }
}
