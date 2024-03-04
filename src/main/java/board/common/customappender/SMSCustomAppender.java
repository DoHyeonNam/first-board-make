package board.common.customappender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Data;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;



import java.util.HashMap;


@Data
public class SMSCustomAppender extends AppenderBase<ILoggingEvent> {
    // ILoggingEvent에 객체를 캡슐화한다(발생한 에러를 전달한다)

    private String apiKey;
    private String apiSecret;
    private String fromPhoneNumber;
    private String toPhoneNumber;

    @Override
    protected void append(ILoggingEvent event){
        if (apiKey.isEmpty() || apiSecret.isEmpty() || fromPhoneNumber.isEmpty() || toPhoneNumber.isEmpty()){
            System.out.println("Missing SMS configuration. Plesase set apiKey, apiSecret, fromPhoneNumber and toPhoneNumber");
            return;
        }

        String logMessage = event.getFormattedMessage(); // getFormattedMessage : 로그 메시지를 가져온다

        Message coolsms = new Message(apiKey, apiSecret); // sms 를 전송하기 위해 생성


        HashMap<String, String> params = new HashMap<>();
        params.put("to", toPhoneNumber);
        params.put("from", fromPhoneNumber);
        params.put("type", "SMS");
        params.put("text", logMessage);
        params.put("version", "Test ver 1.0");

        try {
            coolsms.send(params);
            System.out.println("good");
        }catch (CoolsmsException e){
            System.err.println("Error sending SMS :" + e.getMessage());
         }

    }
}
