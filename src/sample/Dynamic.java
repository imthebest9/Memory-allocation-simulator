package sample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class Dynamic implements Initializable, EventHandler<KeyEvent> {
    @FXML private HBox qbox=new HBox();
    @FXML private VBox mbox=new VBox();
    @FXML private Button s=new Button();
    @FXML private TextArea ta=new TextArea();
    @FXML private Button r=new Button();
    @FXML private Button ev=new Button();
    @FXML private Label t=new Label();
    @FXML private AnchorPane pane;
    ArrayList<job> joblist = new ArrayList<job> (); //array list to store the job from the text file
    ArrayList<memory> memorylist = new ArrayList<memory> (); //array list to create and combine dynamic partition
    // array list for dynamic memory block in system
    ArrayList<job> waitingQueue = new ArrayList<job> (); //array list to store the jobs in the waiting queue
    ArrayList<Integer> event = new ArrayList<Integer> (); //array list to store the event time
    ArrayList<index> indexArrayList = new ArrayList<index> (); //array list to store the index and memory block size to sort the memory block
    public static int noOfJob, choice,totalprocessingtime, memorySize,timer,w;
    public static int minQTime=1, maxQTime=1, totalQTime=0,count=0; //variables to store minimum, maximum and total queue time
    public static double lastTime, jobNo=0, totalQlength=0,totalExFrag=0;
    public static ArrayList<queue> queueLengthlist = new ArrayList<queue>(); //array list to store the queue length and time

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        r.setOnAction(null);
        w=0; jobNo=0; totalQlength=0;totalExFrag=0;
        minQTime=1; maxQTime=1; totalQTime=0;count=0; //variables to store minimum, maximum and total queue time
    }

    @Override
    public void handle(KeyEvent event) {
        showevent();
    }

    public class job {
        int jobNo, arrivalTime, processingTime, jobSize, waitingTime, startTime;
        boolean jobComplete, status;

        public job(int a, int b, int c, int d, boolean e) {
            jobNo = a;
            arrivalTime = b;
            processingTime = c;
            jobSize = d;
            status = e;
        }

        public void setWaitingTime(int a) {
            waitingTime = a;
        }

        public void setStatus(boolean a) {
            status = a;
        }

        public void setStartTime(int a) {
            startTime = a;
        }

        public void setJobComplete(boolean a) {
            jobComplete = a;
        }

        public int getJobNo() {
            return jobNo;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getProcessingTime() {
            return processingTime;
        }

        public int getJobSize() {
            return jobSize;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public boolean getStatus() {
            return status;
        }

        public boolean getJobComplete() {
            return jobComplete;
        }
    }

    public class index
    {
        int index, blockSize;

        public index(int a, int b)
        {
            index=a;
            blockSize=b;
        }

        public int getIndex() {return index;}

        public int getBlockSize() {return blockSize;}
    }

    public class memory {
        int status, blockSize, endTime, jobNo, blockNo;
        boolean occupied;
        public memory(int a, int b, int c, int d, boolean e) {
            status = a;
            blockSize = b;
            endTime = c;
            jobNo = d;
            occupied=e;
        }
        public void setOccupied(boolean a){occupied=a;}
        public void setBlockSize(int a) {
            blockSize = a;
        }

        public void setBlockNo(int a) {
            blockNo = a;
        }

        public void setJobNo(int a) {
            jobNo = a;
        }

        public void setStatus(int a) {
            status = a;
        }

        public void setEndTime(int a) {
            endTime = a;
        }

        public int getBlockSize() {
            return blockSize;
        }

        public int getStatus() {
            return status;
        }

        public int getJobNo() {
            return jobNo;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getBlockNo() {
            return blockNo;
        }
    }



    public void readjob(String filePath) //function to read the job text file and store the jobs to joblist
    {
        File file=new File(filePath);
        try {
            Scanner scan=new Scanner(file);
            noOfJob=scan.nextInt();
            while(scan.hasNextLine()) {
                int jobnum=scan.nextInt();
                int arrivalt=scan.nextInt();
                int processingt=scan.nextInt();
                int jobsize=scan.nextInt();
                joblist.add(new job(jobnum, arrivalt, processingt, jobsize, false));
            }
            scan.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        s.setOnAction(null);
    }

    public void addevent(int a) {	//store the arrival time into array list
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
    } //end function to add new event time if the event time is not in the current event time

    public void setAscendingIndex() //sort the array list that store the memory block size and index in ascending order
    {
        indexArrayList.clear ();
        for(int y=0; y<memorylist.size ();y++)
        {
            indexArrayList.add(new index (y, memorylist.get(y).getBlockSize()));
            for(int i=0; i<indexArrayList.size()-1; i++)
            {
                for (int j=0; j<indexArrayList.size()-i-1; j++)
                {
                    if(indexArrayList.get(j).getBlockSize() > indexArrayList.get(j+1).getBlockSize ())
                    {
                        index tempVar = indexArrayList.get(j+1);
                        indexArrayList.set(j+1, indexArrayList.get(j));
                        indexArrayList.set(j,tempVar);
                    }
                }
            }
        }
    }

    public void setDescendingIndex() //sort the array list that store the memory block size and index in descending order
    {
        indexArrayList.clear ();
        for(int y=0; y<memorylist.size ();y++)
        {
            indexArrayList.add(new index (y, memorylist.get(y).getBlockSize()));
            for(int i=0; i<indexArrayList.size()-1; i++)
            {
                for (int j=0; j<indexArrayList.size()-i-1; j++)
                {
                    if(indexArrayList.get(j).getBlockSize() < indexArrayList.get(j+1).getBlockSize ())
                    {
                        index tempVar = indexArrayList.get(j+1);
                        indexArrayList.set(j+1, indexArrayList.get(j));
                        indexArrayList.set(j,tempVar);
                    }
                }
            }
        }
    }

    public class queue{
        int length;
        public queue(int a)
        {
            length=a;
        }

        public int getLength(){return length ;}
    }

    public void clear() //reset all the global array list
    {
        joblist.clear();
        memorylist.clear();
        indexArrayList.clear();
        waitingQueue.clear();
        event.clear();
        queueLengthlist.clear();
    }

    public void start() {
        Scanner scan=new Scanner(System.in);
        readjob("Joblist.txt"); //read the job list text file and add all the job to job array list
        //variables to store last event time, total processed job number, total queue length and total external fragmentation

        for (int w = 0; w < joblist.size (); w++) //add all the jobs' arrival time to the event time
            addevent (joblist.get (w).getArrivalTime ());

        // collect data from user to set the memory block size


        //add the whole memory block size to array list as first element
        memorylist.add (new memory (-1, memorySize, -10, 0,false)); //add the first memory block
        Label l=new Label("BLOCK SIZE="+memorySize);
        l.setFont(new Font("Arial", 15));
        mbox.getChildren().addAll(new Rectangle(memorySize/100,20, Color.GREEN),l);
        ev.setOnAction((event)->showevent());
        r.setOnAction(null);
        pane.setOnKeyPressed(this);
    }


    public void showevent(){
        ta.setEditable(false);
        if(w<event.size()&&event.size()!=0) {
            mbox.getChildren().clear();
            timer = event.get(w); //set the current time to current event time
            System.out.println("Time " + timer);
            ta.appendText("Time " + timer+"\n");
            for (int y = 0; y < memorylist.size(); y++) //check if there is job that can be free
            {
                if (memorylist.get(y).getEndTime() == timer) //if the time to free the memory block = current time
                {
                    memorylist.get(y).setOccupied(false);
                    memorylist.get(y).setEndTime(-10); //set the time to free the memory block to an unreachable time
                    memorylist.get(y).setStatus(-1); //set the block status to free
                    for (int z = 0; z < joblist.size(); z++) {
                        //search for the job in the job list to set the job to clear
                        if (joblist.get(z).getJobNo() == memorylist.get(y).getJobNo()) {
                            System.out.println("----------------------------------------------");
                            System.out.println("Job " + memorylist.get(y).getJobNo() + " is completed");
                            ta.appendText("----------------------------------------------\n");
                            ta.appendText("Job " + memorylist.get(y).getJobNo() + " is completed\n");
                            joblist.get(z).setJobComplete(true); //set job to clear
                            joblist.get(z).setWaitingTime(timer - joblist.get(z).getArrivalTime() - joblist.get(z).getProcessingTime()); //set the job waiting time
                        }
                    }
                }
            }

            //check if there is two consecutive free blocks that can be combined
            for (int y = 0; y < memorylist.size(); y++) {
                for (int z = 0; z < memorylist.size() - y - 1; z++) {
                    if (memorylist.get(z).getStatus() == -1 && memorylist.get(z + 1).getStatus() == -1) {
                        memorylist.get(z).setBlockSize(memorylist.get(z).getBlockSize() + memorylist.get(z + 1).getBlockSize());
                        memorylist.remove(z + 1);
                        z--;
                    }
                }
            }


            if (choice == 4)
                setAscendingIndex(); //best fit
            else if (choice == 5)
                setDescendingIndex(); //worst fit
            else ; //first fit

            if (choice == 4 || choice == 5) //for best and worst fit
            {
                for (int x = 0; x < waitingQueue.size(); x++) {
                    for (int y = 0; y < indexArrayList.size(); y++) {
                        //if the memory block is free and memory block size is greater or equal than job size
                        if (memorylist.get(indexArrayList.get(y).getIndex()).getStatus() == -1 &&
                                memorylist.get(indexArrayList.get(y).getIndex()).getBlockSize() >= waitingQueue.get(x).getJobSize()) {
                            System.out.println("==============================================");
                            System.out.println("Job " + waitingQueue.get(x).getJobNo() + " in the waiting queue has been processing");
                            System.out.println("Arrival Time    : " + waitingQueue.get(x).getArrivalTime());
                            System.out.println("Processing Time : " + waitingQueue.get(x).getProcessingTime());
                            System.out.println("Start Time      : " + timer);
                            System.out.println("End Time        : " + (waitingQueue.get(x).getProcessingTime() + timer));
                            System.out.println("Job Size        : " + waitingQueue.get(x).getJobSize() + "\n");
                            ta.appendText("==============================================\n");
                            ta.appendText("Job " + waitingQueue.get(x).getJobNo() + " in the waiting queue has been processing\n");
                            ta.appendText("Arrival Time    : " + waitingQueue.get(x).getArrivalTime()+"\n");
                            ta.appendText("Processing Time : " + waitingQueue.get(x).getProcessingTime()+"\n");
                            ta.appendText("Start Time      : " + timer+"\n");
                            ta.appendText("End Time        : " + (waitingQueue.get(x).getProcessingTime() + timer)+"\n");
                            ta.appendText("Job Size        : " + waitingQueue.get(x).getJobSize() + "\n");

                            //determine the maximum queue time and minimum queue time
                            int m = timer - waitingQueue.get(x).getArrivalTime();
                            if (m > maxQTime) {
                                maxQTime = m;
                            }

                            if (m < minQTime && m != 0) {
                                minQTime = m;
                            }
                            totalQTime += m;

                            // set the current memory block size to current block size-job size
                            memorylist.get(indexArrayList.get(y).getIndex()).setBlockSize
                                    (memorylist.get(indexArrayList.get(y).getIndex()).getBlockSize() - waitingQueue.get(x).getJobSize());
                            memorylist.get(indexArrayList.get(y).getIndex()).setOccupied(false);
                            //add the time to free the memory block to the event time
                            addevent(timer + waitingQueue.get(x).getProcessingTime());
                            //add a new memory block in front of current memory block
                            memorylist.add((indexArrayList.get(y).getIndex()), new memory(1, waitingQueue.get(x).getJobSize(),
                                    timer + waitingQueue.get(x).getProcessingTime(), waitingQueue.get(x).getJobNo(),true));
                            totalprocessingtime+=waitingQueue.get(x).getProcessingTime(); //star
                            if (choice == 4)
                                setAscendingIndex(); //best fit
                            else
                                setDescendingIndex(); //worst fit

                            for (int z = 0; z < joblist.size(); z++) //search for the job to set the start time
                            {
                                if (joblist.get(z).getJobNo() == waitingQueue.get(x).getJobNo())
                                    joblist.get(z).setStartTime(timer); //set the job start time to current time
                            }
                            count=0;
                            for(Node child:qbox.getChildren()){
                                if(count==x)
                                {
                                    qbox.getChildren().remove(x);break;}
                                count++;
                            }
                            waitingQueue.remove(x); //remove the job from the waiting queue
                            x--;
                            break; //stop looping the index array list
                        }

                    }
                }//waiting queue
            }    //end best fit and worst fit

            else //first fit
            {
                for (int x = 0; x < waitingQueue.size(); x++) {
                    for (int y = 0; y < memorylist.size(); y++) {
                        //if there the memory block is free and memory block size greater or equal to job size
                        if (memorylist.get(y).getStatus() == -1 && memorylist.get(y).getBlockSize()
                                >= waitingQueue.get(x).getJobSize()) {
                            System.out.println("==============================================");
                            System.out.println("Job " + waitingQueue.get(x).getJobNo() + " in the waiting queue is being processed");
                            System.out.println("Arrival Time    : " + waitingQueue.get(x).getArrivalTime());
                            System.out.println("Processing Time : " + waitingQueue.get(x).getProcessingTime());
                            System.out.println("Start Time      : " + timer);
                            System.out.println("End Time        : " + (waitingQueue.get(x).getProcessingTime() + timer));
                            System.out.println("Job Size        : " + waitingQueue.get(x).getJobSize());
                            ta.appendText("==============================================\n");
                            ta.appendText("Job " + waitingQueue.get(x).getJobNo() + " in the waiting queue is being processed\n");
                            ta.appendText("Arrival Time    : " + waitingQueue.get(x).getArrivalTime()+"\n");
                            ta.appendText("Processing Time : " + waitingQueue.get(x).getProcessingTime()+"\n");
                            ta.appendText("Start Time      : " + timer+"\n");
                            ta.appendText("End Time        : " + (waitingQueue.get(x).getProcessingTime() + timer)+"\n");
                            ta.appendText("Job Size        : " + waitingQueue.get(x).getJobSize()+"\n");

                            //determine the maximum, minimum queue time
                            int m = timer - waitingQueue.get(x).getArrivalTime();
                            if (m > maxQTime) {
                                maxQTime = m;
                            }
                            if (m < minQTime && m != 0) {
                                minQTime = m;
                            }
                            //calculate total queue time
                            totalQTime += m;

                            //set the memory block size to block size- job size
                            memorylist.get(y).setBlockSize(memorylist.get(y).getBlockSize() - waitingQueue.get(x).getJobSize());
                            memorylist.get(y).setOccupied(false);
                            //add the time to free the memory block to event time
                            addevent(timer + waitingQueue.get(x).getProcessingTime());
                            //add a new memory block in front of current memory block
                            memorylist.add(y, new memory(1, waitingQueue.get(x).getJobSize(),
                                    timer + waitingQueue.get(x).getProcessingTime(), waitingQueue.get(x).getJobNo(),true));
                            totalprocessingtime+=waitingQueue.get(x).getProcessingTime();
                            for (int z = 0; z < joblist.size(); z++) //search for the job in the job list
                            {
                                if (joblist.get(z).getJobNo() == waitingQueue.get(x).getJobNo())
                                    joblist.get(z).setStartTime(timer); //set the job start time
                            }
                            count=0;
                            for(Node child:qbox.getChildren()){
                                if(count==x)
                                {
                                    qbox.getChildren().remove(x);break;}
                                count++;
                            }
                            waitingQueue.remove(x); //remove the job from the waiting queue
                            x--;
                            break; //stop looping the memory block
                        }
                    }//end of memory block loop
                }//end of waiting queue loop
            }

            if (choice == 4)
                setAscendingIndex(); //best fit
            else if (choice == 5)
                setDescendingIndex(); //worst fit
            else ; //first fit

            if (choice == 4 || choice == 5) //best fit and worst fit
            {
                for (int x = 0; x < joblist.size(); x++) {
                    for (int y = 0; y < indexArrayList.size(); y++) {
                        //if the job arrival time = current time and flag control is false
                        if (!joblist.get(x).getStatus() && joblist.get(x).getArrivalTime() == timer) {
                            //if the memory block is free and the block size is greater or equal to the job size
                            if (memorylist.get(indexArrayList.get(y).getIndex()).getStatus() == -1 &&
                                    memorylist.get(indexArrayList.get(y).getIndex()).getBlockSize() >= joblist.get(x).getJobSize()) {
                                System.out.println("______________________________________________");
                                System.out.println("Job " + joblist.get(x).getJobNo() + " is being processed");
                                System.out.println("Arrival time           : " + joblist.get(x).getArrivalTime());
                                System.out.println("Processing time        : " + joblist.get(x).getProcessingTime());
                                System.out.println("End time               : " + (joblist.get(x).getProcessingTime() + joblist.get(x).getArrivalTime()));
                                System.out.println("Job Size               : " + joblist.get(x).getJobSize() + "\n");
                                ta.appendText("______________________________________________\n");
                                ta.appendText("Job " + joblist.get(x).getJobNo() + " is being processed\n");
                                ta.appendText("Arrival time           : " + joblist.get(x).getArrivalTime()+"\n");
                                ta.appendText("Processing time        : " + joblist.get(x).getProcessingTime()+"\n");
                                ta.appendText("End time               : " + (joblist.get(x).getProcessingTime() + joblist.get(x).getArrivalTime())+"\n");
                                ta.appendText("Job Size               : " + joblist.get(x).getJobSize() + "\n");

                                //set the current memory block size to memory block size - job size
                                memorylist.get(indexArrayList.get(y).getIndex()).setBlockSize
                                        (memorylist.get(indexArrayList.get(y).getIndex()).getBlockSize() - joblist.get(x).getJobSize());
                                memorylist.get(indexArrayList.get(y).getIndex()).setOccupied(false);
                                joblist.get(x).setStatus(true); //set the flag control to true
                                joblist.get(x).setStartTime(timer); //set the job start time to current time

                                addevent(timer + joblist.get(x).getProcessingTime()); //add the time to free memory block to event time
                                //add a new memory block in front of current block
                                memorylist.add(indexArrayList.get(y).getIndex(), new memory(1, joblist.get(x).getJobSize(),
                                        timer + joblist.get(x).getProcessingTime(), joblist.get(x).getJobNo(),true));
                                totalprocessingtime+=joblist.get(x).getProcessingTime();
                                if (choice == 4) //best fit
                                    setAscendingIndex(); //sort the memory block in index array list in ascending order
                                else //worst fit
                                    setDescendingIndex(); //sort the memory block in index array list in descending order

                                break; //stop looping the index array list
                            }
                        } else
                            break; //stop looping the job array list
                    }

                    //if the arrival time of job = current time and flag control is false
                    if (!joblist.get(x).getStatus() && joblist.get(x).getArrivalTime() == timer) {
                        waitingQueue.add(joblist.get(x)); //add the job to waiting queue
                        StackPane stack = new StackPane();
                        Label l=new Label("J"+joblist.get(x).getJobNo());
                        l.setFont(new Font("Arial", 15));
                        stack.getChildren().addAll(new Rectangle(50,50,Color.YELLOW),l );
                        qbox.getChildren().add(stack);
                        joblist.get(x).setStatus(true); //set flag control to true
                    }
                } //end job loop
            } //end of if

            else //first fit
            {
                for (int x = 0; x < joblist.size(); x++) {
                    for (int y = 0; y < memorylist.size(); y++) {
                        //if the job arrival time = current time and flag control is false
                        if (!joblist.get(x).getStatus() && joblist.get(x).getArrivalTime() == timer) {
                            //if the memory block is free and the block size is greater or equal to the job size
                            if (memorylist.get(y).getStatus() == -1 && memorylist.get(y).getBlockSize()
                                    >= joblist.get(x).getJobSize()) {

                                System.out.println("______________________________________________");
                                System.out.println("Job " + joblist.get(x).getJobNo() + " is being processed");
                                System.out.println("Arrival time           : " + joblist.get(x).getArrivalTime());
                                System.out.println("Processing time        : " + joblist.get(x).getProcessingTime());
                                System.out.println("End time               : " + (joblist.get(x).getProcessingTime()
                                        + joblist.get(x).getArrivalTime()));
                                System.out.println("Job Size               : " + joblist.get(x).getJobSize());

                                ta.appendText("______________________________________________\n");
                                ta.appendText("Job " + joblist.get(x).getJobNo() + " is being processed\n");
                                ta.appendText("Arrival time           : " + joblist.get(x).getArrivalTime()+"\n");
                                ta.appendText("Processing time        : " + joblist.get(x).getProcessingTime()+"\n");
                                ta.appendText("End time               : " + (joblist.get(x).getProcessingTime()
                                        + joblist.get(x).getArrivalTime())+"\n");
                                ta.appendText("Job Size               : " + joblist.get(x).getJobSize()+"\n");



                                memorylist.get(y).setBlockSize(memorylist.get(y).getBlockSize() - joblist.get(x).getJobSize());
                                memorylist.get(y).setOccupied(false);
                                joblist.get(x).setStatus(true);
                                joblist.get(x).setStartTime(timer);
                                addevent(timer + joblist.get(x).getProcessingTime());
                                memorylist.add(y, new memory(1, joblist.get(x).getJobSize(),
                                        timer + joblist.get(x).getProcessingTime(), joblist.get(x).getJobNo(),true));
                                totalprocessingtime+=joblist.get(x).getProcessingTime();
                                break; //stop looping memory block
                            }
                        } else
                            break; //stop looping job list
                    }

                    //if the arrival time of job = current time and flag control is false
                    if (!joblist.get(x).getStatus() && joblist.get(x).getArrivalTime() == timer) {
                        System.out.println("______________________________________________");
                        System.out.println("Job " + joblist.get(x).getJobNo() + " is being moved to Waiting Queue");
                        System.out.println();


                        ta.appendText("______________________________________________\n");
                        ta.appendText("Job " + joblist.get(x).getJobNo() + " is being moved to Waiting Queue\n");

                        waitingQueue.add(joblist.get(x)); //add the job to waiting queue
                        StackPane stack = new StackPane();
                        Label l=new Label("J"+joblist.get(x).getJobNo());
                        l.setFont(new Font("Arial", 15));
                        stack.getChildren().addAll(new Rectangle(40,40,Color.YELLOW), l);
                        qbox.getChildren().add(stack);
                        joblist.get(x).setStatus(true);
                    }
                }//end of job list
            }//end of else

            //print the memory block details
            for (int y = 0; y < memorylist.size(); y++) {
                int x = y + 1;
                System.out.print("Block " + x + " block size " + memorylist.get(y).getBlockSize() + " status ");
                ta.appendText("Block " + x + " block size " + memorylist.get(y).getBlockSize() + " status ");
                if (memorylist.get(y).getStatus() == 1) {
                    System.out.println("BUSY");
                    ta.appendText("BUSY\n");
                }
                else
                {
                    System.out.println("FREE");
                    ta.appendText("FREE\n");
                }
            }
            System.out.println("\n");

            //if the waiting queue is not empty add next time to event time
            if (!waitingQueue.isEmpty()) {
                System.out.println("          Waiting Queue");
                System.out.println("----------------------------------");
                System.out.println("Job Number            Job Size");
                ta.appendText("          Waiting Queue\n");
                ta.appendText("----------------------------------\n");
                ta.appendText("Job Number            Job Size\n");
                for (int x = 0; x < memorylist.size(); x++) {
                    //calculate the total external fragmentation
                    if (memorylist.get(x).getStatus() == -1)
                        totalExFrag += memorylist.get(x).getBlockSize();
                }
                //print the waiting queue
                for (int a = 0; a < waitingQueue.size(); a++) {
                    System.out.println("   " + waitingQueue.get(a).getJobNo() + "                  " + waitingQueue.get(a).getJobSize());
                    ta.appendText("   " + waitingQueue.get(a).getJobNo() + "                  " + waitingQueue.get(a).getJobSize()+"\n");
                }
                System.out.println();
            }
            queueLengthlist.add(new queue(waitingQueue.size())); //add queue length to array list every time unit
            System.out.println();
            for (int z = 0; z < memorylist.size(); z++) {
                if(memorylist.get(z).occupied)
                {
                    Label l=new Label("BLOCK SIZE="+memorylist.get(z).getBlockSize()+", Job "+memorylist.get(z).getJobNo());
                    l.setFont(new Font("Arial", 15));
                    mbox.getChildren().addAll(new Rectangle(memorylist.get(z).getBlockSize()/100,20, Color.RED),l);}
                else
                {
                    Label l=new Label("BLOCK SIZE="+memorylist.get(z).getBlockSize());
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
        if(w==event.size()) {
            System.out.format("%" + 50 + "s", "Job\n");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.println("Job    Arrival Time    Processing Time    Job Size    Start Time    Waiting Time    Processed");
            ta.appendText("Job\n");
            ta.appendText("-----------------------------------------------------------------------------------------------------\n");
            ta.appendText("Job    Arrival Time    Processing Time    Job Size    Start Time    Waiting Time    Processed\n");
            for (int a = 0; a < joblist.size(); a++) {
                System.out.format("%2s%9s%18s%19s%12s%14s%17s", joblist.get(a).getJobNo(), joblist.get(a).getArrivalTime(),
                        joblist.get(a).getProcessingTime(), joblist.get(a).getJobSize(), joblist.get(a).getStartTime(), joblist.get(a).getWaitingTime(),
                        joblist.get(a).getJobComplete());
                System.out.println();

                ta.appendText(String.format("%2s%9s%22s%33s%14s%20s%20s", joblist.get(a).getJobNo(), joblist.get(a).getArrivalTime(),
                        joblist.get(a).getProcessingTime(), joblist.get(a).getJobSize(), joblist.get(a).getStartTime(), joblist.get(a).getWaitingTime(),
                        joblist.get(a).getJobComplete()));
                ta.appendText("\n");
            }
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.println("\n\n");
            System.out.format("%" + 30 + "s", "Output\n");
            ta.appendText("======================================================================================\n");
            ta.appendText("-----------------------------------------------------------------------------------------------------\n");
            ta.appendText("\n\n");
            ta.appendText("Output\n");
            ta.appendText("======================================================================================\n");

            //determine the number of job processed
            for (int a = 0; a < joblist.size(); a++) {
                if (joblist.get(a).getJobComplete())
                    jobNo++;
            }

            //sort the queue length array list in ascending order
            for (int i = 0; i < queueLengthlist.size() - 1; i++) {
                for (int j = 0; j < queueLengthlist.size() - i - 1; j++) {
                    if (queueLengthlist.get(j).getLength() > queueLengthlist.get(j + 1).getLength()) {
                        queue tempVar = queueLengthlist.get(j + 1);
                        queueLengthlist.set(j + 1, queueLengthlist.get(j));
                        queueLengthlist.set(j, tempVar);
                    }
                }
            }

            //calculate the total queue length of the simulation
            for (int x = 0; x < queueLengthlist.size(); x++) {
                totalQlength += queueLengthlist.get(x).getLength();
            }

            lastTime = event.get(event.size() - 1); //get the last event time

            ta.appendText("Total Simulation Time: " + lastTime + "\n");
            ta.appendText("Total job completed: " + jobNo + "\n");
            ta.appendText("Total Processing Time: " + totalprocessingtime + "\n");    //XS
            ta.appendText("Throughput: " + String.format("%.3f", totalprocessingtime / lastTime) + " per unit time\n");
            ta.appendText("Total queue length: " + totalQlength + "\n");
            ta.appendText("Maximum queue length: " + queueLengthlist.get(queueLengthlist.size() - 1).getLength() + "\n");
            ta.appendText("Minimum queue length: " + queueLengthlist.get(0).getLength() + "\n");
            ta.appendText("Average queue length: " + String.format("%.3f", totalQlength / lastTime) + "\n");
            ta.appendText("Total queue time: " + totalQTime + "\n");
            ta.appendText("Maximum queue time: " + maxQTime + "\n");
            ta.appendText("Minimum queue time: " + minQTime + "\n");
            ta.appendText("Average queue time: " + String.format("%.3f", (double) totalQTime / noOfJob) + "\n");
            ta.appendText("Total external fragmentation: " + totalExFrag + "\n");
            ta.appendText("Average external fragmentation, versus total memory sizes: " + String.format("%.3f", (double) totalExFrag / noOfJob) + "\n");
            ta.appendText("======================================================================================\n");
            r.setOnAction(null);
            clear();
            ev.setOnAction(null);
            pane.setOnKeyPressed(null);
            totalprocessingtime = 0 ; //XS
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