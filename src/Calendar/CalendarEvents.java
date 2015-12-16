package Calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;


import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yx on 12/13/15.
 */
public class CalendarEvents {
	
	private static final SimpleDateFormat RFC3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
    /** Application name. */
    private static final String APPLICATION_NAME =
            "PiNavigator";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart. */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
        		CalendarEvents.class.getResourceAsStream("client_secret.json");
        if (in==null) System.out.println("!!!!!");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
    
    /**
     * Time converter
     */
    public static String toRFC3339(Calendar cal){
    	cal.add(Calendar.DATE, 1);
    	Date d = cal.getTime();
    	return RFC3339.format(d).replaceAll("(\\d\\d)(\\d\\d)$", "$1,$2");
    }
    
    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
    getCalendarService() throws IOException {

        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String args[]) throws IOException{

    }

    /**
     * The core method to retrive the event information
     * @return a linked list of MyEvent about event information
     * @throws IOException
     */
    public static LinkedList<MyEvent> getEvents() throws IOException{
        com.google.api.services.calendar.Calendar service =
                getCalendarService();

        //Retrieve the calendar
        com.google.api.services.calendar.model.Calendar calendar =
                service.calendars().get("primary").execute();

        //Retrieve the id for the primary calendar
        String primaryCalendarID = calendar.getId();
        System.out.println(primaryCalendarID);

        // Iterate over the events in the specified calendar
        String pageToken = null;
        LinkedList<MyEvent> result = new LinkedList<>();
        do {
            @SuppressWarnings("deprecation")
			Events events = service.events().list("primary").
                    setTimeMin(new DateTime(toRFC3339(Calendar.getInstance()))).   //TODO add start & end Time
                    //setTimeMax(new DateTime(toRFC3339(Calendar.getInstance()))).
                    setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                MyEvent newEvent = new MyEvent(event.getSummary(),
                							   event.getDescription(),
                                               event.getLocation(),
                                               event.getStart().getDateTime().toString(), //event.getStart().getDateTime().toString()   
                                               event.getEnd().getDateTime().toString());  //event.getEnd().getDateTime().toString()
                //For testing
                System.out.println(newEvent.getStartTime());
                result.addLast(newEvent);
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        System.out.println("Events Got!");

        return result;
    }




}
