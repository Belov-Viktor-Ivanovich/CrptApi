package ru.below.crptapi;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.rmi.AccessException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;



@SpringBootApplication
@NoArgsConstructor
public class CrptApiApplication {

    public static  TimeUnit timeUnit;
    public static int counter;
    public static int buf;
    public static Long start ;
    public static Long time ;


    public CrptApiApplication(TimeUnit timeUnit, int counter) {
        CrptApiApplication.timeUnit = timeUnit;
        CrptApiApplication.counter = counter;
        start = System.currentTimeMillis();
        time = Duration.ofHours(timeUnit.getHOUR()).toMillis()+Duration.ofMinutes(timeUnit.getMINUTE()).toMillis()+Duration.ofSeconds(timeUnit.getSECOND()).toMillis();
        buf = counter;
    }

    public static void main(String[] args) {
        CrptApiApplication apiApplication = new CrptApiApplication(new TimeUnit(30L,0L,0L),5);
        SpringApplication.run(CrptApiApplication.class, args);

    }
    public static Optional<Document>createDocument(Document document,String signature) {
        return Optional.of(document);
    }

}
@RestController
@RequestMapping("/api/v3/lk/documents")
@Scope("prototype")
class CrptController {
    //Реализация без сервиса и DTO
    @PostMapping("/create")
    public Optional<?> create(@RequestBody Document document) throws AccessException {
        if (System.currentTimeMillis() - CrptApiApplication.start >= CrptApiApplication.time) {
            CrptApiApplication.start = System.currentTimeMillis();
            CrptApiApplication.buf = CrptApiApplication.counter;
        }
        if (CrptApiApplication.buf <= 0 && System.currentTimeMillis() - CrptApiApplication.start < CrptApiApplication.time)
            throw new AccessException("number of requests exceeded");
        else {
            CrptApiApplication.buf--;
            //Не понятно как использовать подпись
            return CrptApiApplication.createDocument(document, "signature");
        }
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class Document {

    private Description description;
    private String doc_id;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private String production_date;
    private String production_type;
    private List<Product> products;
    private String reg_date;
    private String reg_number;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Description {
    private String participantInn;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Product {
    private String certificate_document;
    private String certificate_document_date;
    private String certificate_document_number;
    private String owner_inn;
    private String producer_inn;
    private String production_date;
    private String tnved_code;
    private String uit_code;
    private String uitu_code;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class TimeUnit {
    private Long SECOND, MINUTE, HOUR;
}
