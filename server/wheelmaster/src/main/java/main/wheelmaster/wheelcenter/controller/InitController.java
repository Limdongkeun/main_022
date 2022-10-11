package main.wheelmaster.wheelcenter.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.wheelmaster.wheelcenter.service.WheelCenterService;
import main.wheelmaster.wheelcenter.entity.WheelCenter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/wheel")
public class InitController {

    private final WheelCenterService service;

    @GetMapping
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JSONObject test1() throws FileNotFoundException {

        final String TRUE = "Y";
        JsonMapper jsonMapper = new JsonMapper();
        int quantity = 100;
        final String serviceKey = "yAXx8wVqlAhGVOpzwgRRwQxkxnafot/V6eRDkU7cJEEcY0X9K4XxkK9nONKC3Imu/Sd0ZqgVfAufMLx9sdJMlA==";
        WebClient client  = WebClient
                .create("http://api.data.go.kr/openapi/tn_pubr_public_electr_whlchairhgh_spdchrgr_api");

        for(int page = 1; page <= 33; ++page) {

            Optional<String> first = client.get()
                    .uri("?serviceKey=" + serviceKey + "&pageNo=" + page +  "&numOfRows=" + quantity +"&type=json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .flux()
                    .toStream()
                    .findFirst();



            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            if (first.isPresent()) {

                JSONObject jsonObject = new JSONObject(first.orElse("{}"));
                System.out.println(jsonObject);
                log.info(jsonObject.toString());
                JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
                int numOfRows = body.getInt("numOfRows");

                if(page == 33) numOfRows = 4;
                JSONArray items = body.getJSONArray("items");



                for (int i = 0; i < numOfRows; ++i) {
                    JSONObject val = items.getJSONObject(i);

                    WheelCenter wheelCenter = WheelCenter.builder()
                            .city(val.getString("ctprvnNm"))
                            .facultyName(val.getString("fcltyNm"))
                            .sigunguName(val.getString("signguNm"))
                            .roadNameAddress(val.getString("rdnmadr"))
                            .lotNumberAddress(val.getString("lnmadr"))
                            .latitude(ensureNumeric(val.get("latitude")))
                            .longitude(ensureNumeric(val.get("longitude")))
                            .installLocation(val.getString("instlLcDesc"))
                            .weekdayOpenHour(LocalTime.parse(hourMinuteFormat(val.getString("weekdayOperOpenHhmm")) , formatter))
                            .weekdayCloseHour(LocalTime.parse(hourMinuteFormat(val.getString("weekdayOperColseHhmm")) , formatter))
                            .satOpenHour(LocalTime.parse(hourMinuteFormat(val.getString("satOperOperOpenHhmm")) , formatter))
                            .satCloseHour(LocalTime.parse(hourMinuteFormat(val.getString("satOperCloseHhmm")) , formatter))
                            .holidayOpenHour(LocalTime.parse(hourMinuteFormat(val.getString("holidayOperOpenHhmm")), formatter))
                            .holidayCloseHour(LocalTime.parse(hourMinuteFormat(val.getString("holidayCloseOpenHhmm")) , formatter))
                            .simultaneousUser(ensureInteger(val.get("smtmUseCo")))
                            .airInjectorFlag(val.getString("airInjectorYn").toUpperCase().equals("Y"))
                            .phoneChargerFlag(val.getString("moblphonChrstnYn").toUpperCase().equals("Y"))
                            .institutionName(val.getString("institutionNm"))
                            .institutionPhoneNumber(val.getString("institutionPhoneNumber"))
                            .build();


                    System.out.println(service.create(wheelCenter));

                }
            }
        }


        return null;

    }

    public String hourMinuteFormat(String time) {
        String[] data = time.split(":");
        Boolean flag = false;

        if (data[0].length() < 2) {
            data[0] = "0" + data[0];

            flag = true;
        }
        if (data[1].length() < 2) {
            data[1] = "0" + data[1];
            flag = true;
        }

        return flag ?
                new String(data[0] + ":" + data[1]) : time;
    }

    public Double ensureNumeric(Object val){
        if (isEmpty(val)) return null;

        return Double.parseDouble((String)val);
    }

    private boolean isEmpty(Object val) {
        if(val == null) return true;
        String str = (String) val;

        if(str.equals("")) return true;
        return false;
    }

    public Integer ensureInteger(Object val){
        if(isEmpty(val)) return null;

        return Integer.parseInt((String)val);
    }

}
