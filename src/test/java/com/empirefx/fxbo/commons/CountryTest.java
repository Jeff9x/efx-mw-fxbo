package com.empirefx.fxbo.commons;
import com.empirefx.fxbo.models.provider.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    private static final String JSON_PAYLOAD = "[\n" +
            "    {\"code\": \"AF\", \"name\": \"Afghanistan\"},\n" +
            "    {\"code\": \"AX\", \"name\": \"Åland Islands\"},\n" +
            "    {\"code\": \"AL\", \"name\": \"Albania\"},\n" +
            "    {\"code\": \"DZ\", \"name\": \"Algeria\"},\n" +
            "    {\"code\": \"AS\", \"name\": \"American Samoa\"},\n" +
            "    {\"code\": \"AD\", \"name\": \"Andorra\"},\n" +
            "    {\"code\": \"AO\", \"name\": \"Angola\"},\n" +
            "    {\"code\": \"AI\", \"name\": \"Anguilla\"},\n" +
            "    {\"code\": \"AQ\", \"name\": \"Antarctica\"},\n" +
            "    {\"code\": \"AG\", \"name\": \"Antigua & Barbuda\"},\n" +
            "    {\"code\": \"AR\", \"name\": \"Argentina\"},\n" +
            "    {\"code\": \"AM\", \"name\": \"Armenia\"},\n" +
            "    {\"code\": \"AW\", \"name\": \"Aruba\"},\n" +
            "    {\"code\": \"AU\", \"name\": \"Australia\"},\n" +
            "    {\"code\": \"AT\", \"name\": \"Austria\"},\n" +
            "    {\"code\": \"AZ\", \"name\": \"Azerbaijan\"},\n" +
            "    {\"code\": \"BS\", \"name\": \"Bahamas\"},\n" +
            "    {\"code\": \"BH\", \"name\": \"Bahrain\"},\n" +
            "    {\"code\": \"BD\", \"name\": \"Bangladesh\"},\n" +
            "    {\"code\": \"BB\", \"name\": \"Barbados\"},\n" +
            "    {\"code\": \"BY\", \"name\": \"Belarus\"},\n" +
            "    {\"code\": \"BE\", \"name\": \"Belgium\"},\n" +
            "    {\"code\": \"BZ\", \"name\": \"Belize\"},\n" +
            "    {\"code\": \"BJ\", \"name\": \"Benin\"},\n" +
            "    {\"code\": \"BM\", \"name\": \"Bermuda\"},\n" +
            "    {\"code\": \"BT\", \"name\": \"Bhutan\"},\n" +
            "    {\"code\": \"BO\", \"name\": \"Bolivia\"},\n" +
            "    {\"code\": \"BA\", \"name\": \"Bosnia & Herzegovina\"},\n" +
            "    {\"code\": \"BW\", \"name\": \"Botswana\"},\n" +
            "    {\"code\": \"BV\", \"name\": \"Bouvet Island\"},\n" +
            "    {\"code\": \"BR\", \"name\": \"Brazil\"},\n" +
            "    {\"code\": \"IO\", \"name\": \"British Indian Ocean Territory\"},\n" +
            "    {\"code\": \"VG\", \"name\": \"British Virgin Islands\"},\n" +
            "    {\"code\": \"BN\", \"name\": \"Brunei\"},\n" +
            "    {\"code\": \"BG\", \"name\": \"Bulgaria\"},\n" +
            "    {\"code\": \"BF\", \"name\": \"Burkina Faso\"},\n" +
            "    {\"code\": \"BI\", \"name\": \"Burundi\"},\n" +
            "    {\"code\": \"KH\", \"name\": \"Cambodia\"},\n" +
            "    {\"code\": \"CM\", \"name\": \"Cameroon\"},\n" +
            "    {\"code\": \"CA\", \"name\": \"Canada\"},\n" +
            "    {\"code\": \"CV\", \"name\": \"Cape Verde\"},\n" +
            "    {\"code\": \"BQ\", \"name\": \"Caribbean Netherlands\"},\n" +
            "    {\"code\": \"KY\", \"name\": \"Cayman Islands\"},\n" +
            "    {\"code\": \"CF\", \"name\": \"Central African Republic\"},\n" +
            "    {\"code\": \"TD\", \"name\": \"Chad\"},\n" +
            "    {\"code\": \"CL\", \"name\": \"Chile\"},\n" +
            "    {\"code\": \"CN\", \"name\": \"China\"},\n" +
            "    {\"code\": \"CX\", \"name\": \"Christmas Island\"},\n" +
            "    {\"code\": \"CC\", \"name\": \"Cocos (Keeling) Islands\"},\n" +
            "    {\"code\": \"CO\", \"name\": \"Colombia\"},\n" +
            "    {\"code\": \"KM\", \"name\": \"Comoros\"},\n" +
            "    {\"code\": \"CG\", \"name\": \"Congo - Brazzaville\"},\n" +
            "    {\"code\": \"CD\", \"name\": \"Congo - Kinshasa\"},\n" +
            "    {\"code\": \"CK\", \"name\": \"Cook Islands\"},\n" +
            "    {\"code\": \"CR\", \"name\": \"Costa Rica\"},\n" +
            "    {\"code\": \"CI\", \"name\": \"Côte d’Ivoire\"},\n" +
            "    {\"code\": \"HR\", \"name\": \"Croatia\"},\n" +
            "    {\"code\": \"CU\", \"name\": \"Cuba\"},\n" +
            "    {\"code\": \"CW\", \"name\": \"Curaçao\"},\n" +
            "    {\"code\": \"CY\", \"name\": \"Cyprus\"},\n" +
            "    {\"code\": \"CZ\", \"name\": \"Czech Republic\"}\n" +
            "]";


    @Test
    void testCountryPayload() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<Country> countries = mapper.readValue(JSON_PAYLOAD, new TypeReference<List<Country>>() {});

        // Assert the total number of countries
        assertEquals(61, countries.size(), "The number of countries should be 61");

        // Assertions for each country in the payload
        assertAll(
                () -> assertEquals("AF", countries.get(0).getCode(), "Code mismatch for Afghanistan"),
                () -> assertEquals("Afghanistan", countries.get(0).getName(), "Name mismatch for Afghanistan"),

                () -> assertEquals("AX", countries.get(1).getCode(), "Code mismatch for Åland Islands"),
                () -> assertEquals("Åland Islands", countries.get(1).getName(), "Name mismatch for Åland Islands"),

                // Continue for each country as follows...
                () -> assertEquals("BZ", countries.get(22).getCode(), "Code mismatch for Belize"),
                () -> assertEquals("Belize", countries.get(22).getName(), "Name mismatch for Belize"),

                () -> assertEquals("BJ", countries.get(23).getCode(), "Code mismatch for Benin"),
                () -> assertEquals("Benin", countries.get(23).getName(), "Name mismatch for Benin"),

                () -> assertEquals("BM", countries.get(24).getCode(), "Code mismatch for Bermuda"),
                () -> assertEquals("Bermuda", countries.get(24).getName(), "Name mismatch for Bermuda"),

                () -> assertEquals("BT", countries.get(25).getCode(), "Code mismatch for Bhutan"),
                () -> assertEquals("Bhutan", countries.get(25).getName(), "Name mismatch for Bhutan"),

                () -> assertEquals("BO", countries.get(26).getCode(), "Code mismatch for Bolivia"),
                () -> assertEquals("Bolivia", countries.get(26).getName(), "Name mismatch for Bolivia"),

                () -> assertEquals("BA", countries.get(27).getCode(), "Code mismatch for Bosnia & Herzegovina"),
                () -> assertEquals("Bosnia & Herzegovina", countries.get(27).getName(), "Name mismatch for Bosnia & Herzegovina"),

                () -> assertEquals("BW", countries.get(28).getCode(), "Code mismatch for Botswana"),
                () -> assertEquals("Botswana", countries.get(28).getName(), "Name mismatch for Botswana"),

                () -> assertEquals("BV", countries.get(29).getCode(), "Code mismatch for Bouvet Island"),
                () -> assertEquals("Bouvet Island", countries.get(29).getName(), "Name mismatch for Bouvet Island"),

                () -> assertEquals("BR", countries.get(30).getCode(), "Code mismatch for Brazil"),
                () -> assertEquals("Brazil", countries.get(30).getName(), "Name mismatch for Brazil"),

                () -> assertEquals("IO", countries.get(31).getCode(), "Code mismatch for British Indian Ocean Territory"),
                () -> assertEquals("British Indian Ocean Territory", countries.get(31).getName(), "Name mismatch for British Indian Ocean Territory"),

                () -> assertEquals("VG", countries.get(32).getCode(), "Code mismatch for British Virgin Islands"),
                () -> assertEquals("British Virgin Islands", countries.get(32).getName(), "Name mismatch for British Virgin Islands"),

                () -> assertEquals("BN", countries.get(33).getCode(), "Code mismatch for Brunei"),
                () -> assertEquals("Brunei", countries.get(33).getName(), "Name mismatch for Brunei"),

                () -> assertEquals("BG", countries.get(34).getCode(), "Code mismatch for Bulgaria"),
                () -> assertEquals("Bulgaria", countries.get(34).getName(), "Name mismatch for Bulgaria"),

                () -> assertEquals("BF", countries.get(35).getCode(), "Code mismatch for Burkina Faso"),
                () -> assertEquals("Burkina Faso", countries.get(35).getName(), "Name mismatch for Burkina Faso"),

                () -> assertEquals("BI", countries.get(36).getCode(), "Code mismatch for Burundi"),
                () -> assertEquals("Burundi", countries.get(36).getName(), "Name mismatch for Burundi"),

                () -> assertEquals("KH", countries.get(37).getCode(), "Code mismatch for Cambodia"),
                () -> assertEquals("Cambodia", countries.get(37).getName(), "Name mismatch for Cambodia"),

                () -> assertEquals("CM", countries.get(38).getCode(), "Code mismatch for Cameroon"),
                () -> assertEquals("Cameroon", countries.get(38).getName(), "Name mismatch for Cameroon"),

                () -> assertEquals("CA", countries.get(39).getCode(), "Code mismatch for Canada"),
                () -> assertEquals("Canada", countries.get(39).getName(), "Name mismatch for Canada"),

                () -> assertEquals("CV", countries.get(40).getCode(), "Code mismatch for Cape Verde"),
                () -> assertEquals("Cape Verde", countries.get(40).getName(), "Name mismatch for Cape Verde"),

                () -> assertEquals("BQ", countries.get(41).getCode(), "Code mismatch for Caribbean Netherlands"),
                () -> assertEquals("Caribbean Netherlands", countries.get(41).getName(), "Name mismatch for Caribbean Netherlands"),

                () -> assertEquals("KY", countries.get(42).getCode(), "Code mismatch for Cayman Islands"),
                () -> assertEquals("Cayman Islands", countries.get(42).getName(), "Name mismatch for Cayman Islands"),

                () -> assertEquals("CF", countries.get(43).getCode(), "Code mismatch for Central African Republic"),
                () -> assertEquals("Central African Republic", countries.get(43).getName(), "Name mismatch for Central African Republic"),

                () -> assertEquals("TD", countries.get(44).getCode(), "Code mismatch for Chad"),
                () -> assertEquals("Chad", countries.get(44).getName(), "Name mismatch for Chad"),

                () -> assertEquals("CL", countries.get(45).getCode(), "Code mismatch for Chile"),
                () -> assertEquals("Chile", countries.get(45).getName(), "Name mismatch for Chile"),

                () -> assertEquals("CN", countries.get(46).getCode(), "Code mismatch for China"),
                () -> assertEquals("China", countries.get(46).getName(), "Name mismatch for China"),

                () -> assertEquals("CX", countries.get(47).getCode(), "Code mismatch for Christmas Island"),
                () -> assertEquals("Christmas Island", countries.get(47).getName(), "Name mismatch for Christmas Island"),

                () -> assertEquals("CC", countries.get(48).getCode(), "Code mismatch for Cocos (Keeling) Islands"),
                () -> assertEquals("Cocos (Keeling) Islands", countries.get(48).getName(), "Name mismatch for Cocos (Keeling) Islands"),

                () -> assertEquals("CO", countries.get(49).getCode(), "Code mismatch for Colombia"),
                () -> assertEquals("Colombia", countries.get(49).getName(), "Name mismatch for Colombia"),

                () -> assertEquals("KM", countries.get(50).getCode(), "Code mismatch for Comoros"),
                () -> assertEquals("Comoros", countries.get(50).getName(), "Name mismatch for Comoros"),

                () -> assertEquals("CG", countries.get(51).getCode(), "Code mismatch for Congo - Brazzaville"),
                () -> assertEquals("Congo - Brazzaville", countries.get(51).getName(), "Name mismatch for Congo - Brazzaville"),

                () -> assertEquals("CD", countries.get(52).getCode(), "Code mismatch for Congo - Kinshasa"),
                () -> assertEquals("Congo - Kinshasa", countries.get(52).getName(), "Name mismatch for Congo - Kinshasa"),

                () -> assertEquals("CK", countries.get(53).getCode(), "Code mismatch for Cook Islands"),
                () -> assertEquals("Cook Islands", countries.get(53).getName(), "Name mismatch for Cook Islands"),

                () -> assertEquals("CR", countries.get(54).getCode(), "Code mismatch for Costa Rica"),
                () -> assertEquals("Costa Rica", countries.get(54).getName(), "Name mismatch for Costa Rica"),

                () -> assertEquals("CI", countries.get(55).getCode(), "Code mismatch for Côte d’Ivoire"),
                () -> assertEquals("Côte d’Ivoire", countries.get(55).getName(), "Name mismatch for Côte d’Ivoire"),

                () -> assertEquals("HR", countries.get(56).getCode(), "Code mismatch for Croatia"),
                () -> assertEquals("Croatia", countries.get(56).getName(), "Name mismatch for Croatia"),

                () -> assertEquals("CU", countries.get(57).getCode(), "Code mismatch for Cuba"),
                () -> assertEquals("Cuba", countries.get(57).getName(), "Name mismatch for Cuba"),

                () -> assertEquals("CW", countries.get(58).getCode(), "Code mismatch for Curaçao"),
                () -> assertEquals("Curaçao", countries.get(58).getName(), "Name mismatch for Curaçao"),

                () -> assertEquals("CY", countries.get(59).getCode(), "Code mismatch for Cyprus"),
                () -> assertEquals("Cyprus", countries.get(59).getName(), "Name mismatch for Cyprus"),

                () -> assertEquals("CZ", countries.get(60).getCode(), "Code mismatch for Czech Republic"),
                () -> assertEquals("Czech Republic", countries.get(60).getName(), "Name mismatch for Czech Republic")
        );


    }
}