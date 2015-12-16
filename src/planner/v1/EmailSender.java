package planner.v1;

import TurnByTurn.Step;

import java.util.LinkedList;

/**
 * Created by yx on 12/7/15.
 */
public class EmailSender {
      public static void sendDirections(LinkedList<LinkedList<Step>> steps,String toUser){
          LinkedList<String> msg = new LinkedList<String>();
          String tempmsg;
          for (LinkedList<Step> e1:steps) {
              tempmsg = "";
              for (int i=0; i < e1.size();i++) {
                  tempmsg += (i+1)+" "+e1.get(i).getMessage();
                  tempmsg += "<br>";
              }
              msg.addLast(tempmsg);
          }

          SendEmailTLS.sendToEmail(msg,toUser);
      }
}