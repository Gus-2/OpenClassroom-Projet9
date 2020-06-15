package com.openclassrooms.realestatemanager.ui.realestateform;

import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;

import java.util.ArrayList;
import java.util.List;

public class ToolsUpdateData {

    public static Address processAddress(Address address, String street, String number, String district, String city, String postCode, String additionalInformation){
        String streetProcessed = processAddressString(street);
        String numberProcessed = processAddressString(number);
        String districtProcessed = processAddressString(district);
        String cityProcessed = processAddressString(city);
        String postCodeProcessed = processAddressString(postCode);
        String additionnalInformationProcessed = processAddressString(additionalInformation);
        if(!streetProcessed.equals(address.getStreet()) || !numberProcessed.equals(address.getNumber()) || !districtProcessed.equals(address.getDistrict())
        || !cityProcessed.equals(address.getCity()) || !postCodeProcessed.equals(address.getPostCode()) || !additionnalInformationProcessed.equals(address.getAdditionalInformation())){
            Address updatedAddress = new Address(streetProcessed, numberProcessed, districtProcessed, cityProcessed, address.getCountry(), postCodeProcessed, additionnalInformationProcessed);
            updatedAddress.setIdAddress(address.getIdAddress());
            return updatedAddress;
        }
        return null;
    }

    public static House processHouse(House house, long idHouseType, long idRealEstateAgent, String price, String surface, String description, String state, long availableDate){
        double priceProcessed = processPriceAndSurfaceString(price);
        double surfaceProcessed = processPriceAndSurfaceString(surface);
        String descriptionProcessed = processDescriptionString(description);

        if(idHouseType != house.getIdHouseType() || idRealEstateAgent != house.getIdRealEstateAgent() || priceProcessed != house.getPrice() ||
                surfaceProcessed != house.getSurface() || !house.getDescription().equals(description) || availableDate != house.getAvailableDate()){
            House houseUpdated = new House(idHouseType, idRealEstateAgent, priceProcessed, surfaceProcessed, descriptionProcessed, state, availableDate);
            houseUpdated.setIdHouse(house.getIdHouse());
            houseUpdated.setIdAddress(house.getIdAddress());
            return houseUpdated;
        }
        return null;
    }

    public static List<RoomNumber> processRoom(long idHouse, List<RoomNumber> listRoomNumbers, String numberKitchen, String numberBathroom, String numberBedroom, String numberLivingRoom, String numberToilet, String numberCellar, String numberPool){
        List<RoomNumber> listRoomNumbersProcessed = new ArrayList<>();
        listRoomNumbersProcessed.add(new RoomNumber(1, Integer.parseInt(numberKitchen)));
        listRoomNumbersProcessed.add(new RoomNumber(2, Integer.parseInt(numberBathroom)));
        listRoomNumbersProcessed.add(new RoomNumber(3, Integer.parseInt(numberBedroom)));
        listRoomNumbersProcessed.add(new RoomNumber(4, Integer.parseInt(numberLivingRoom)));
        listRoomNumbersProcessed.add(new RoomNumber(5, Integer.parseInt(numberToilet)));
        listRoomNumbersProcessed.add(new RoomNumber(6, Integer.parseInt(numberCellar)));
        listRoomNumbersProcessed.add(new RoomNumber(7, Integer.parseInt(numberPool)));

        if(listRoomNumbersProcessed.get(0).getNumber() != listRoomNumbers.get(0).getNumber() || listRoomNumbersProcessed.get(1).getNumber() != listRoomNumbers.get(1).getNumber() ||
        listRoomNumbersProcessed.get(2).getNumber() != listRoomNumbers.get(2).getNumber() || listRoomNumbersProcessed.get(3).getNumber() != listRoomNumbers.get(3).getNumber() ||
        listRoomNumbersProcessed.get(4).getNumber() != listRoomNumbers.get(4).getNumber() || listRoomNumbersProcessed.get(5).getNumber() != listRoomNumbers.get(5).getNumber() ||
                listRoomNumbersProcessed.get(6).getNumber() != listRoomNumbers.get(6).getNumber()){
            for(int i = 0; i < listRoomNumbersProcessed.size(); i++)
                listRoomNumbersProcessed.get(i).setIdHouse(idHouse);
            return listRoomNumbersProcessed;
        }
        return null;
    }


    static String processAddressString(String string){
        if(string.equals("Unspecified")){
            return "";
        }else if(string.equals("")){
            return "";
        }else{
            return string;
        }
    }

    static double processPriceAndSurfaceString(String string){
        if(string.equals("NS")){
            return -1;
        }else if(string.equals("")){
            return -1;
        }else{
            string = string.replace(",", "");
            return Double.parseDouble(string);
        }
    }

    static String processDescriptionString(String string){
        if(string.equals("Description unspecified")){
            return "";
        }else{
            return string;
        }
    }
}
