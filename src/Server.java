import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;

    private ArrayList<UserProfile> list;
    private ArrayList<Message> messagelist;

    private Socket connection;
    private String emailsFileLocation;
    private String hangedMessagesFileLocation;
    private String groupsFileLocation;


    public boolean addUser(UserProfile user){return true;}
    public boolean checkIfUserNameAvailable(String userName){return true;}

    public boolean deleteUser(UserProfile user){return true;}
    public boolean updateUser(UserProfile user){return true;}
    public void rejectRequest(request request){}
    public boolean creatGroup(ArrayList<UserProfile> list){return true;}
    public boolean deleteGroup(String groupName){return true;}
    public boolean updateGroup(String groupName){
        return false;
    }
    public boolean checkIfGroupNameAvailabe(String groupName){return true;}
    public void hangeMessage(){}
    public Message freeMessage(String messageId){return null;}



    public void handleRequst(request request){
        requestType type = request.getType();

        switch (type)
        {
            case ADD_USER:
            {
                if(!addUser((UserProfile) request.getObject())){
                    rejectRequest(request);
                }
                break;
            }
            case DELETE_USER:
            {
               if(!deleteUser((UserProfile) request.getObject())){
                   rejectRequest(request);
               }
                break;
            }
            case UPDATE_USER:
            {
                updateUser((UserProfile) request.getObject());
            }
            case CREATE_GROUP:
            {
                if(!creatGroup((ArrayList<UserProfile>) request.getObject()))
                    rejectRequest(request);
                break;
            }
            case DELETE_GROUP:
            {
                if(!deleteGroup((String) request.getObject()))
                    rejectRequest(request);
                break;
            }
            case UPDATE_GROUP:
            {
                if(!updateGroup((String) request.getObject()))
                    rejectRequest(request);
                break;
            }
            case SEND_MESSAGE:
            {

            }
            case CHECK_HANGED_MESSAGES:
            {

            }
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

    public Server() throws IOException {

        //messagelist = ObjectOutputStream(hangedMessagesFileLocation)
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
        final String[] message = {""};
        String recieveMessage = "";
        do { //have a conversation
           recieveMessage = (String) input.readObject();
           if(recieveMessage!=null)
           {System.out.println(recieveMessage);
           }
        } while (!message[0].equals("CLIENT - END")); // the while will excite until any one type END then the chat will stop here we deal with Just String

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
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}
