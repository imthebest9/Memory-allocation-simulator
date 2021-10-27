package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;
public class Fixed implements Initializable, EventHandler<KeyEvent> {
    @FXML private TextArea ta;
    @FXML private VBox mbox=new VBox();
    @FXML private HBox qbox=new HBox();
    @FXML private Button s=new Button();
    @FXML private Button r=new Button();
    @FXML private Button ev=new Button();
    @FXML private Label t=new Label();
    @FXML private AnchorPane pane;
    Scanner scan=new Scanner(System.in);
    ArrayList<Job> joblist = new ArrayList<Job> ();  //array list to store jobs from joblist text file
    ArrayList<Memory> memorylist = new ArrayList<Memory> (); //array list to store the memory block from memorylist text file
    ArrayList<Job> waitingQueue = new ArrayList<Job> (); //array list to store job added to the waiting queue
    ArrayList<Integer> event = new ArrayList<Integer> (); //array list to store the event time
    ArrayList<queue> queueLength = new ArrayList<queue>(); //array list to store queue length and time
    public static int noOfJob, noOfPartition, choice,optblock,totalprocesstime=0;
    public static int minQTime=1, maxQTime=1, totalQTime=0; //variables to store minimum, maximum and total queue time
    public static double noJob=0, totalQueueLength = 0; //variables to store the number of jobs and total queue length
    public static int timer,w=0,count; //clock time
    public static double totalInternal1 = 0, totalMemorySize=0; //variable to store total internal fragmentation
    public static double lastTime =0; //variable to store the last event time

    public void readjob(String filePath){

        File file=new File(filePath);
        try {
            Scanner scan=new Scanner(file);
            noOfJob=scan.nextInt();
            while(scan.hasNextLine()) {
                int jobnum=scan.nextInt();
                int arrivalt=scan.nextInt();
                int processingt=scan.nextInt();
                int jobsize=scan.nextInt();
                joblist.add(new Job(jobnum, arrivalt, processingt, jobsize, false));
            }
            scan.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }//function to read the job text file and store the jobs to joblist

    public void removeJobLargerThanMemory() //function to remove the job that is larger than the largest memory block
    {
        int largest = 0;
        for(int a=0; a<memorylist.size(); a++)
        {
            if(memorylist.get(a).getBlockSize()>largest)
                largest = memorylist.get(a).getBlockSize();
        }
        for(int z=0; z<joblist.size(); z++)
        {
            if(joblist.get(z).getJobSize()>largest)
                joblist.get(z).setBool(true);
        }
    }

    public void readmemory(String filePath,int optblock){
        File file=new File(filePath);
        try {
            Scanner scan=new Scanner(file);
            noOfPartition=scan.nextInt();
            int block=1;
            while(scan.hasNextLine()&&block<=optblock) {
                int msize=scan.nextInt();
                memorylist.add(new Memory(block, msize, false));
                block++;
            }
            scan.close();
            for (int z = 0; z < memorylist.size(); z++) {
                if(memorylist.get(z).occupied)
                {
                    Label l=new Label(memorylist.get(z).getBlockSize()+"(BLOCK "+memorylist.get(z).getBlockNo()+")");
                    l.setFont(new Font("Arial", 15));
                    mbox.getChildren().addAll(new Rectangle(memorylist.get(z).getBlockSize()/100,20,Color.RED),l);}
                else
                {
                    Label l=new Label("BLOCK SIZE="+memorylist.get(z).getBlockSize()+"(BLOCK "+memorylist.get(z).getBlockNo()+")");
                    l.setFont(new Font("Arial", 15));
                    mbox.getChildren().addAll(new Rectangle(memorylist.get(z).getBlockSize()/100,20,Color.GREEN),l);}
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        s.setOnAction(null);
    }//function to read the memory text file and store the memory block to memorylist

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        r.setOnAction(null);
        w=0;
        minQTime=1; maxQTime=1; totalQTime=0;
        noJob=0; totalQueueLength = 0;
        w=0;count=0;
        totalInternal1 = 0; totalMemorySize=0; //variable to store total internal fragmentation
        lastTime =0; //variable to store the last event time
    }

    @Override
    public void handle(KeyEvent event) {
        showevent();
    }

    public class Job {
        int jobNo, arrivalTime, processingTime, jobSize, waitingTime, startTime;
        boolean jobClear, bool;

        public Job(int a, int b, int c, int d, boolean e) {
            jobNo = a;
            arrivalTime = b;
            processingTime = c;
            jobSize = d;
            bool = e;
        }

        public void setWaitingTime(int a) {
            waitingTime = a;
        }

        public void setBool(boolean e) {
            bool = e;
        }

        public void setStartTime(int b) {
            startTime = b;
        }

        public void setJobClear(boolean c) {
            jobClear = c;
        }

        public int getJobNo() {
            return jobNo;
        }

        public int getStartTime() { return startTime; }

        public int getWaitingTime() {return waitingTime;}

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getProcessingTime() {
            return processingTime;
        }

        public int getJobSize() {
            return jobSize;
        }

        public boolean getBool() {
            return bool;
        }

        public boolean getJobClear() {
            return jobClear;
        }

    }

    public class Memory {
        boolean occupied;
        int blockNo, blockSize, freeTimer, jobNo;

        public Memory(int a, int b, boolean o) {
            blockNo = a;
            blockSize = b;
            occupied = o;
            freeTimer = -10;
        }

        public void setBlockSize(int a) {
            blockSize = a;
        }

        public void setJobNo(int c) {
            jobNo = c;
        }

        public void setOccupied(boolean c) {
            occupied = c;
        }

        public void setFreeTimer(int d) {
            freeTimer = d;
        }

        public int getBlockSize() {
            return blockSize;
        }

        public boolean getOccupied() {
            return occupied;
        }

        public int getBlockNo() {
            return blockNo;
        }

        public int getJobNo() {
            return jobNo;
        }

        public int getFreeTimer() {
            return freeTimer;
        }

    }

    public class queue{
        int length,time;

        public queue(int a, int b)
        {
            length=a;
            time=b;
        }

        public int getLength(){return length ;}

    }

    public void bubbleSort() //function to sort the memory block in ascending order
    {
        for(int i=0; i<memorylist.size()-1; i++)
        {
            for (int j=0; j<memorylist.size()-i-1; j++)
            {
                if(memorylist.get(j).getBlockSize() > memorylist.get(j+1).getBlockSize ())
                {
                    Memory tempVar = memorylist.get(j+1);
                    memorylist.set(j+1, memorylist.get(j));
                    memorylist.set(j,tempVar);
                }
            }
        }
    }

    public void addevent(int a) {
        if (event.isEmpty ())
            event.add (a);
        else {
            boolean found = false;
            for (int x = 0; x < event.size (); x++) {
                if (event.get (x) == a)
                    found = true;
            }
            if (!found) {
                event.add (a);
                Collections.sort (event);
            }
        }
    } //function to add new event time if the event time is not in the current event time

    public void clear(){
        joblist.clear();
        memorylist.clear();
        waitingQueue.clear();
        event.clear();
    } //reset the all the global array list

    public void start(){
        ev.setOnAction((event)->showevent());
        readjob("Joblist.txt"); //add all the jobs in the text file to the job array list
        readmemory ("MemoryList.txt",optblock); //add all the memory block in the text file to the memory array list
        removeJobLargerThanMemory(); //remove the job greater than the largest memory block

        if(choice==2) //best fit
            bubbleSort(); //sort the memory block in ascending order

        for (int w = 0; w < joblist.size (); w++) //add all the arrival time to the event clock
            addevent (joblist.get (w).getArrivalTime ());
        r.setOnAction(null);
        pane.setOnKeyPressed(this);
    } //first fit and best fit of fixed partition



    public void showevent(){
        ta.setEditable(false);
        if(w<event.size()) {
            mbox.getChildren().clear();
            System.out.print("Time " + event.get(w) + "\n");
            timer = event.get(w);  //set current time to the current event time
            ta.appendText("Time " + event.get(w) + "\n");
            //check if the is a memory block that can be freed
            //check memory block
            for (int x = 0; x < memorylist.size(); x++) {
                if (memorylist.get(x).getFreeTimer() == timer) //if the time to free the memory block = current time
                {
                    memorylist.get(x).setFreeTimer(-10); //set the time to free the memory block to an unreachable time
                    memorylist.get(x).setOccupied(false); //set the memory block status to free
                    ta.appendText("----------------------------------------------\n");
                    ta.appendText("Job " + memorylist.get(x).getJobNo() + " is completed, Memory Block " +
                            memorylist.get(x).getBlockNo() + " is freed\n");
                    for (int y = 0; y < joblist.size(); y++) //search the the job number
                    {
                        if (memorylist.get(x).getJobNo() == joblist.get(y).getJobNo()) {
                            joblist.get(y).setJobClear(true); //set the job freed from the memory block to clear
                            joblist.get(y).setWaitingTime(timer - joblist.get(y).getProcessingTime() - joblist.get(y).getArrivalTime()); //set the job waiting time
                        }
                    }
                }
            }

            //process waiting queue
            for (int y = 0; y < waitingQueue.size(); y++) {
                for (int z = 0; z < memorylist.size(); z++) {
                    //if the memory block is free and the job size is smaller than the memory block size
                    if (!memorylist.get(z).getOccupied() && waitingQueue.get(y).getJobSize() <= memorylist.get(z).getBlockSize()) {
                        ta.appendText("==============================================\n");
                        ta.appendText("Job " + waitingQueue.get(y).getJobNo() + " in the waiting queue is being processed\n");
                        ta.appendText("Arrival time           : " + waitingQueue.get(y).getArrivalTime() + "\n");
                        ta.appendText("Start Time             : " + event.get(w) + "\n");
                        ta.appendText("Processing time        : " + waitingQueue.get(y).getProcessingTime() + "\n");
                        ta.appendText("End time               : " + (waitingQueue.get(y).getProcessingTime() + event.get(w)) + "\n");
                        ta.appendText("Job Size               : " + waitingQueue.get(y).getJobSize() + "\n");
                        ta.appendText("Memory Block           : " + memorylist.get(z).getBlockNo() + "\n");
                        ta.appendText("Memory Size            : " + memorylist.get(z).getBlockSize() + "\n");
                        ta.appendText("Internal Fragmentation : " + ((memorylist.get(z).getBlockSize() - waitingQueue.get(y).getJobSize())) + "\n");
                        //determine the maximum queue time minimum queue time
                        if ((timer - waitingQueue.get(y).getArrivalTime()) > maxQTime) {
                            maxQTime = timer - waitingQueue.get(y).getArrivalTime();
                        }
                        if ((timer - waitingQueue.get(y).getArrivalTime()) <= minQTime && (timer - waitingQueue.get(y).getArrivalTime()) != 0) {
                            minQTime = timer - waitingQueue.get(y).getArrivalTime();
                        }
                        //calculate the total queue time
                        totalQTime += (timer - waitingQueue.get(y).getArrivalTime());
                        System.out.println(timer - waitingQueue.get(y).getArrivalTime());
                        //determine whether the memory block is heavily, normal or under used

                        //setting variable
                        memorylist.get(z).setJobNo(waitingQueue.get(y).getJobNo()); //set the current job number to the memory block
                        memorylist.get(z).setOccupied(true); //set the memory block to busy
                        count=0;


                        //set the time to free the memory block
                        memorylist.get(z).setFreeTimer(timer + waitingQueue.get(y).getProcessingTime());
                        // total processing time
                        totalprocesstime+=waitingQueue.get(y).getProcessingTime();
                        //add the time to free the memory block to the event time
                        addevent(timer + waitingQueue.get(y).getProcessingTime());
                        //calculate the total internal fragmentation
                        totalInternal1 += ((memorylist.get(z).getBlockSize() - waitingQueue.get(y).getJobSize()));

                        for (int a = 0; a < joblist.size(); a++) {
                            if (joblist.get(a).getJobNo() == waitingQueue.get(y).getJobNo())
                                joblist.get(a).setStartTime(timer); //set the start time of the job
                        }

                        count=0;
                        for(Node child:qbox.getChildren()){
                            if(count==y)
                            {
                                qbox.getChildren().remove(y);break;}
                            count++;
                        }

                        waitingQueue.remove(y); //remove the job from waiting queue
                        y--;
                        break; //stop looping the memory array list
                    }
                }
            } //end of waiting queue
            //process job list
            for (int x = 0; x < joblist.size(); x++) {
                for (int y = 0; y < memorylist.size(); y++) {
                    //if the job arrival time = current time and the flag control is false
                    if (!joblist.get(x).getBool() && joblist.get(x).getArrivalTime() == timer) {
                        //if the memory block is free and memory block size is greater or equal to job size
                        if (!memorylist.get(y).getOccupied() && memorylist.get(y).getBlockSize() >= joblist.get(x).getJobSize()) {
                            ta.appendText("______________________________________________\n");
                            ta.appendText("Job " + joblist.get(x).getJobNo() + " is being processed\n");
                            ta.appendText("Arrival time           : " + joblist.get(x).getArrivalTime() + "\n");
                            ta.appendText("Processing time        : " + joblist.get(x).getProcessingTime() + "\n");
                            ta.appendText("End time               : " + (joblist.get(x).getProcessingTime() + joblist.get(x).getArrivalTime()) + "\n");
                            ta.appendText("Job Size               : " + joblist.get(x).getJobSize() + "\n");
                            ta.appendText("Memory Block           : " + (memorylist.get(y).getBlockNo()) + "\n");
                            ta.appendText("Memory Size            : " + (memorylist.get(y).getBlockSize()) + "\n");
                            ta.appendText("Internal Fragmentation : " + ((memorylist.get(y).getBlockSize() - joblist.get(x).getJobSize())) + "\n");
                            //setting variable
                            memorylist.get(y).setOccupied(true); //set the memory block to busy
                            memorylist.get(y).setJobNo(joblist.get(x).getJobNo()); //set the current job number to memory block
                            //set the time to free the memory block
                            memorylist.get(y).setFreeTimer(timer + joblist.get(x).getProcessingTime());
                            //add the time to free the memory block to event time
                            addevent(memorylist.get(y).getFreeTimer());
                            //total processing time
                            totalprocesstime+=joblist.get(x).getProcessingTime();
                            //set the start time of the job
                            joblist.get(x).setStartTime(timer);
                            //set the flag control to true
                            joblist.get(x).setBool(true);
                            //calculate the total internal fragmentation
                            totalInternal1 += ((memorylist.get(y).getBlockSize() - joblist.get(x).getJobSize()));
                            break; //stop looping the memory block
                        }
                    } else
                        break; //stop looping the job list
                }


                //if the job arrival time = current time & the job is not processed
                if (!joblist.get(x).getBool() && joblist.get(x).getArrivalTime() == timer) {
                    waitingQueue.add(joblist.get(x));  //add the job to waiting queue
                    StackPane stack = new StackPane();
                    Label l=new Label("J"+joblist.get(x).getJobNo());
                    l.setFont(new Font("Arial", 15));
                    stack.getChildren().addAll(new Rectangle(50,50, Color.YELLOW),l);
                    qbox.getChildren().add(stack);
                    joblist.get(x).setBool(true); //set the flag control to true
                }

            } //end of job list
            //print the memory block details
            ta.appendText("           Memory Block\n");
            ta.appendText("----------------------------------\n");
            for (int y = 0; y < memorylist.size(); y++) {
                ta.appendText("Block " + memorylist.get(y).getBlockNo() + " block size " + memorylist.get(y).getBlockSize() + " status ");
                if (memorylist.get(y).getOccupied())
                    ta.appendText("BUSY\n");
                else
                    ta.appendText("FREE\n");
            }

            System.out.println("\n");

            //print the waiting queue details
            if (!waitingQueue.isEmpty()) {
                ta.appendText("          Waiting Queue\n");
                ta.appendText("----------------------------------\n");
                ta.appendText("Job Number            Job Size\n");
                for (int a = 0; a < waitingQueue.size(); a++) {
                    ta.appendText("   " + waitingQueue.get(a).getJobNo() + "                  " + waitingQueue.get(a).getJobSize() + "\n");
                }
                System.out.println();
            }

            if (!waitingQueue.isEmpty())
                addevent(timer + 1); //add next time unit to event time to check the waiting queue

            //add the waiting queue length and current time to the queue length array list
            queueLength.add(new queue(waitingQueue.size(), timer));
            System.out.println();
            for (int z = 0; z < memorylist.size(); z++) {
                if(memorylist.get(z).occupied)
                {
                    int frag=0;
                    int tempsize=0;
                    for(int b=0;b<joblist.size();b++){
                        if(memorylist.get(z).getJobNo()==joblist.get(b).getJobNo()){
                            tempsize=joblist.get(b).getJobSize();
                            frag=memorylist.get(z).getBlockSize()-joblist.get(b).getJobSize();
                            break;
                        }
                    }
                    Label l=new Label("BLOCK SIZE="+memorylist.get(z).getBlockSize()+"(BLOCK "+
                            memorylist.get(z).getBlockNo()+")"+", Job "+memorylist.get(z).getJobNo()+"  -  "+frag+" fragmentation");
                    l.setFont(new Font("Arial", 15));
                    StackPane jobmemory= new StackPane();
                    Rectangle mrec=new Rectangle(memorylist.get(z).getBlockSize()/100,20,Color.GREEN);
                    Rectangle jrec=new Rectangle(tempsize/100,20,Color.RED);
                    jobmemory.getChildren().addAll(mrec,jrec);
                    jobmemory.setAlignment(mrec, Pos.TOP_LEFT);
                    jobmemory.setAlignment(jrec, Pos.TOP_LEFT);
                    mbox.getChildren().addAll(jobmemory,l);}
                else
                {
                    Label l=new Label("BLOCK SIZE="+memorylist.get(z).getBlockSize()+"(BLOCK "+memorylist.get(z).getBlockNo()+")");
                    l.setFont(new Font("Arial", 15));
                    mbox.getChildren().addAll(new Rectangle(memorylist.get(z).getBlockSize()/100,20,Color.GREEN),l);}
            }
            t.setText("Time "+timer);
            t.setFont(new Font("Arial", 15));
            w++;
        }
        else
            r.setOnAction((event) -> result());
    }

    public void result(){
        if(w==event.size()&&event.size()!=0) {
            //print the job list details
            System.out.format("%" + 50 + "s", "Job\n");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.println("Job    Arrival Time    Processing Time    Job Size    Start Time    Waiting Time    Processed");
            ta.appendText("Job\n");
            ta.appendText("-----------------------------------------------------------------------------------------------------\n");
            ta.appendText("Job    Arrival Time    Processing Time    Job Size    Start Time    Waiting Time    Processed\n");
            for (int a = 0; a < joblist.size(); a++) {
                System.out.format("%2s%9s%18s%19s%12s%14s%17s", joblist.get(a).getJobNo(), joblist.get(a).getArrivalTime(),
                        joblist.get(a).getProcessingTime(), joblist.get(a).getJobSize(), joblist.get(a).getStartTime(), joblist.get(a).getWaitingTime(),
                        joblist.get(a).getJobClear());
                System.out.println();
                ta.appendText(String.format("%2s%9s%22s%33s%14s%20s%20s", joblist.get(a).getJobNo(), joblist.get(a).getArrivalTime(),
                        joblist.get(a).getProcessingTime(), joblist.get(a).getJobSize(), joblist.get(a).getStartTime(), joblist.get(a).getWaitingTime(),
                        joblist.get(a).getJobClear()));
                ta.appendText("\n");
            }
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.println("\n\n");
            System.out.format("%" + 30 + "s", "Output\n");
            System.out.println("======================================================================================\n");
            ta.appendText("-----------------------------------------------------------------------------------------------------\n");
            ta.appendText("\n\n");
            ta.appendText("Output\n");
            ta.appendText("======================================================================================\n");
            //Determine the number of jobs completed at the end of event
            for (int y = 0; y < joblist.size(); y++) {
                if (joblist.get(y).getJobClear())
                    noJob++;
            }

            //Sort the queue length array list based on the length in ascending order
            for (int i = 0; i < queueLength.size() - 1; i++) {
                for (int j = 0; j < queueLength.size() - i - 1; j++) {
                    if (queueLength.get(j).getLength() > queueLength.get(j + 1).getLength()) {
                        queue tempVar = queueLength.get(j + 1);
                        queueLength.set(j + 1, queueLength.get(j));
                        queueLength.set(j, tempVar);
                    }
                }
            }

            //calculate the total queue length by looping the queue length array list
            for (int x = 0; x < queueLength.size(); x++) {
                totalQueueLength += queueLength.get(x).getLength();
                System.out.println(queueLength.get(x).getLength());
            }

            //calculate the total memory block size
            for (int a = 0; a < memorylist.size(); a++) {
                totalMemorySize += memorylist.get(a).getBlockSize();
            }

            //get the last event time
            lastTime = event.get(event.size() - 1);
            ta.appendText("Total Stimulation Time: " + lastTime + "\n");
            ta.appendText("Total job completed: " + noJob + "\n");
            ta.appendText("Total queue length: " + totalQueueLength + "\n");
            ta.appendText("Total queue time: " + totalQTime + "\n");
            ta.appendText("Total Processing Time: " + totalprocesstime + "\n");    //XS
            ta.appendText("Throughput: " + String.format("%.3f", totalprocesstime / lastTime) + " per unit time\n");
            ta.appendText("Average queue length: " + String.format("%.3f", totalQueueLength / lastTime) + "\n");
            ta.appendText("Minimum queue length: " + queueLength.get(0).getLength() + "\n");
            ta.appendText("Maximum queue length: " + queueLength.get(queueLength.size() - 1).getLength() + "\n");
            ta.appendText("Average queue waiting time: " + String.format("%.3f", totalQTime / noJob) + "\n");
            ta.appendText("Minimum queue waiting time: " + minQTime + "\n");
            ta.appendText("Maximum queue waiting time: " + maxQTime + "\n");
            ta.appendText("Total internal fragmentation: " + totalInternal1 + "\n");
            ta.appendText("Average internal fragmentation: " + String.format("%.3f", (double) totalInternal1 / noJob) + " fragmentation per job\n");
            ta.appendText("======================================================================================\n");
            r.setOnAction(null);
            clear();
            ev.setOnAction(null);
            pane.setOnKeyPressed(null);
            totalprocesstime = 0 ; // XS
        }
    }
    public void tomainmenu(ActionEvent event) throws IOException {
        Parent menuparent= FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }

}

