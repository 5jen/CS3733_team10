package planner.v1;

import TurnByTurn.Step;

import java.util.LinkedList;

/**
 * Created by yx on 12/7/15.
 */
public class EmailSender {
      public static void sendDirections(LinkedList<Step> steps,String toUser){
          String msg="Thank's for using PiNavigatior! \n\n\n";
          for (Step s: steps){
              msg += s.getMessage();
              msg += "\n\n";
          }
          msg+="Thanks for using our app!\n\n";
          SendEmailTLS.sendToEmail(msg,toUser);
      }
}
