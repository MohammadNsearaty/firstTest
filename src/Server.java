import com.sun.javafx.runtime.async.AbstractRemoteResource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;

    private ArrayList<UserProfile> list = new ArrayList<>();
    private ArrayList<Message> messagelist;
    private ArrayList<Group> groupsList;

    private Socket connection;
    private String emailsFileLocation ="C:\\Users\\Mohamad Nsearaty\\Desktop\\hangeFile";
    private String hangedMessagesFileLocation ="C:\\Users\\Mohamad Nsearaty\\Desktop\\hangedMessages";
    private String groupsFileLocation= "C:\\Users\\Mohamad Nsearaty\\Desktop\\groupsFile";

    public Server() throws IOException, ClassNotFoundException {

        emailsFileLocation ="C:\\Users\\Mohamad Nsearaty\\Desktop\\hangeFile";
        hangedMessagesFileLocation ="C:\\Users\\Mohamad Nsearaty\\Desktop\\hangedMessages";
        groupsFileLocation= "C:\\Users\\Mohamad Nsearaty\\Desktop\\groupsFile";

        FileInputStream emailFile = new FileInputStream(emailsFileLocation);
        ObjectInputStream emailStream = new ObjectInputStream(emailFile);
        FileInputStream messagesFile = new FileInputStream(hangedMessagesFileLocation);
        ObjectInputStream messagesStream = new ObjectInputStream(messagesFile);
        FileInputStream groupFile = new FileInputStream(groupsFileLocation);
        ObjectInputStream groupStream = new ObjectInputStream(groupFile);
        list = (ArrayList<UserProfile>) emailStream.readObject();
        messagelist = (ArrayList<Message>) messagesStream.readObject();
        groupsList = (ArrayList<Group>) groupStream.readObject();
    }



    public boolean addUser(UserProfile user){
        for(int i =0 ; i < list.size() ;i++)
        {
            if(list.get(i).getUserId().equals(user.getUserId()))
                return false;
        }
        list.add(user);
        return true;
    }
    public boolean checkIfUserNameAvailable(String userName){return true;}
    public boolean deleteUser(UserProfile user){return true;}
    public boolean updateUser(UserProfile user){return true;}
    public void rejectRequest(ArrayList<Object> list) throws IOException {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(list.get(0));
        arrayList.add(false);
        output.writeObject(arrayList);
        output.flush();
    }
    public void successfulRequest(ArrayList<Object> list) throws IOException {
        ArrayList<Object> arrayList = new ArrayList<>();
        UserProfile profile = serilaizeProfileObject(list);
        addUser(profile);
        arrayList.add(list.get(0));
        arrayList.add(true);
        arrayList.add("abc123");//Test Id
        output.writeObject(arrayList);
        output.flush();
    }
    public boolean createGroup(Group group){
        if(!checkIfGroupNameAvaillabe(group.getName()))
            return false;
        groupsList.add(group);
        return true;
    }
    public boolean deleteGroup(Group group){
        if(checkIfGroupNameAvaillabe(group.getName()))
           return false;
        groupsList.remove(group);
        return true;
    }
    public boolean updateGroup(Group group){
        if(checkIfGroupNameAvaillabe(group.getName()))
            return false;
        for(Group gr:groupsList)
        {
            if(gr.getId() == group.getId())
            {
                groupsList.remove(gr);
                groupsList.add(group);
            }
        }
        return false;
    }
    public boolean checkIfGroupNameAvaillabe(String groupName){
        for(Group gr:groupsList)
        {
           if(groupName ==gr.getName())
               return false;
        }
        return true;
    }

    public void hangMessage(Message message){
        messagelist.add(message);
    }
    public Message freeMessage(String messageId){

        for(Message message:messagelist)
        {
            if(message.getMessageID().equals(messageId))
            {
                messagelist.remove(message);
                return message;
            }
        }
        return null;
    }
    public ArrayList<Message> searchInHangedMessages(String userId)
    {
        ArrayList<Message> userMessages = new ArrayList<>();
        for(Message message: messagelist)
            if(message.getRecieverID().equals(userId))
                userMessages.add(freeMessage(message.getMessageID()));
        return userMessages;
    }
    public UserProfile serilaizeProfileObject(ArrayList<Object> arrayList)
    {
        UserProfile userProfile = new UserProfile();

        userProfile.setUserName((String) arrayList.get(1));
        userProfile.setEmail((String) arrayList.get(2));
        userProfile.setPassword((String) arrayList.get(3));
        userProfile.setBirthDate((Date) arrayList.get(4));
        userProfile.setJoinDate((Date)arrayList.get(5));
        userProfile.setUserId("abc123");


        return userProfile;
    }
    public void handleRequest(ArrayList<Object> list) throws IOException {
        int type = (int) list.get(0);
        switch (type)
        {
            case 0://Add user
            {
                UserProfile userProfile = serilaizeProfileObject(list);
                if(!addUser(userProfile)){
                    rejectRequest(list);
                }
                else
                   successfulRequest(list);
                break;
            }/*
            case DELETE_USER:
            {
               if(!deleteUser((UserProfile) request.getObject())){
                   rejectRequest(request);
               }
               else
                   successfulRequest(request);
                break;
            }
            case UPDATE_USER:
            {
                updateUser((UserProfile) request.getObject());
                successfulRequest(request);
            }
            case CREATE_GROUP:
            {
                if(!createGroup((Group) request.getObject()))
                    rejectRequest(request);
                else
                    successfulRequest(request);
                break;
            }
            case DELETE_GROUP:
            {
                if(!deleteGroup((Group) request.getObject()))
                    rejectRequest(request);
                else
                    successfulRequest(request);
                break;
            }
            case UPDATE_GROUP:
            {
                if(!updateGroup((Group) request.getObject()))
                    rejectRequest(request);
                else
                    successfulRequest(request);
                break;
            }
            case SEND_MESSAGE:
            {
                hangMessage((Message) request.getObject());
                successfulRequest(request);
            }
            case CHECK_HANGED_MESSAGES:
            {
                ArrayList<Message> userMessages = searchInHangedMessages((String) request.getObject());
                if(userMessages.size() == 0)
                    rejectRequest(request);
                else
                {
                    request.setObject(userMessages);
                    output.writeObject(request);
                    output.flush();
                }
            }*/
        }

    }
    public String getEmailsFileLocation() {
        return emailsFileLocation;
    }

    public void setEmailsFileLocation(String emailsFileLocation) {
        this.emailsFileLocation = emailsFileLocation;
    }

    public String getHangedMessagesFileLocation() {
        return hangedMessagesFileLocation;
    }

    public void setHangedMessagesFileLocation(String hangedMessagesFileLocation) {
        this.hangedMessagesFileLocation = hangedMessagesFileLocation;
    }


    public ObjectOutputStream getOutputStream()
    {
        return output;
    }
    public ObjectInputStream getInputStream()
    {
        return input;
    }



    //Set Up and run the server
    public void startRunning() throws IOException {
        server = new ServerSocket(6789, 100);
        while (true) {
            try {
                WaitForConnection();  //wait someone to connect with me
                SetUpStream();// after one connect with me I Will setup my Stream InputStream and OutPutStream and setup connection
                WhileChatting(); // the programme that will send and receive message
            } catch (EOFException eof) {
                eof.printStackTrace();
                System.out.println("Stop:" + "\n" + "The Server End up the Connection...");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                CloseCrap();
            }

        }
    }

    //wait for connection then display connection information
    private void WaitForConnection()throws IOException{
        System.out.println("Waiting for someone to connect...");
        connection = server.accept(); // to accept any one want to chat with you
    }

    //make the stream to send and receive the message
    private void SetUpStream()throws IOException{

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        output.writeObject(null);
        System.out.println("The Stream Is Ready");
    }

    //The function will execute during the chat
    private void WhileChatting() throws IOException, ClassNotFoundException {
        System.out.println("Begin Chating");

    /*    FileInputStream fis = new FileInputStream("C:\\Users\\Mohamad Nsearaty\\Desktop\\One Piece wallpapers\\[Al3asq] One Piece - 876 [h264 1080p 10bit].mkv_snapshot_21.31_[2019.03.23_16.05.42].jpg");
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        output.writeObject(buffer);
        output.flush();*/
        //final String message[] = {""};
        ArrayList<Object> recieveMessage;
        input.readObject();
       // do { //have a conversation
           recieveMessage = (ArrayList<Object>) input.readObject();
            System.out.println(recieveMessage);
           //if(recieveMessage!=)
           //{
               handleRequest(recieveMessage);
          // }

        //} while (!message[0].equals("CLIENT - END")); // the while will excite until any one type END then the chat will stop here we deal with Just String

    }


    //Send Message to client
    public void SendMessage(String message){
        try{
         //   System.out.println("SendMessage() was called");
            System.out.println("Server - "+message);
            output.writeObject("SERVER - "+message); //send message throw the output stream
            output.flush();
            ShowMessage("\n SERVER -"+message);
        }catch (IOException e){ // if we can't send message to reason
            e.printStackTrace();
            System.out.println("Something IS Wrong With Sending Message");

        }

    }
    //Update Chat Window
    private void ShowMessage(final String Text){ }


    private void AbleToType(final boolean type){ // if it true the user can type and if it false the user can't type if there is no one connect with him
        //type on the chat list if  type = true
        //make the Edit text UserText.setEditable(type);

    }

    //Close Streams and Socket after you done The Chatting
    private void CloseCrap(){
        ShowMessage("\n Closing Connection...\n");
        // here shut the able to write message
        try{
            output.close();
            input.close();
            connection.close();
            File emailFile = new File(emailsFileLocation);
            emailFile.createNewFile();

            File hangeFile = new File(hangedMessagesFileLocation);
            hangeFile.createNewFile();

            File groupFile = new File(groupsFileLocation);
            groupFile.createNewFile();


            FileOutputStream hangeStream = new FileOutputStream(hangeFile);
            FileOutputStream usersStream = new FileOutputStream(emailFile);
            FileOutputStream groupStream = new FileOutputStream(groupFile);

            ObjectOutputStream hangedMessagesWriter = new ObjectOutputStream(hangeStream);
            ObjectOutputStream usersWriter = new ObjectOutputStream(usersStream);
            ObjectOutputStream groupWriter = new ObjectOutputStream(groupStream);

            hangedMessagesWriter.writeObject(messagelist);
            usersWriter.writeObject(list);
            groupWriter.writeObject(groupsList);

            hangedMessagesWriter.flush();
            usersWriter.flush();
            groupWriter.flush();

            hangedMessagesWriter.close();
            usersWriter.close();
            groupWriter.close();

            hangeStream.close();
            usersStream.close();
            groupStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}
