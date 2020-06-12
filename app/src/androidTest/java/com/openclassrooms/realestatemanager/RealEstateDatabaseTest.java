package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateDatabaseTest {

    private RealEstateDatabase realEstateDatabase;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        realEstateDatabase = Room.inMemoryDatabaseBuilder(context, RealEstateDatabase.class).build();
    }

    // TEST address TABLE
    private static Address ADDRESS1 = new Address("Rue des choux-fleurs", "15A", "New York",
            "United-States", "1310", "East Side", "bte 2");
    private static final int ADDRESS1_ID = 1;

    @Test
    public void insertAndGetAddress() throws InterruptedException {
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        Address address = LiveDataTestUtil.getValue(this.realEstateDatabase.addressDao().getAddress(ADDRESS1_ID));
        assertTrue(address.getCity().equals(ADDRESS1.getCity()) && address.getIdAddress() == ADDRESS1_ID);
    }

    // TEST type_point_of_interest TABLE
    private static TypePointOfInterest TYPE_POINT_OF_INTEREST1 = new TypePointOfInterest("School");
    private static TypePointOfInterest TYPE_POINT_OF_INTEREST2 = new TypePointOfInterest("Airport");
    private static int TYPE_POINT_OF_INTEREST_ID1 = 1;

    @Test
    public void insertAndGetTypePointOfInterest() throws InterruptedException {
        this.realEstateDatabase.typePointOfInterestDao().insertTypePointOfInterest(TYPE_POINT_OF_INTEREST1);
        this.realEstateDatabase.typePointOfInterestDao().insertTypePointOfInterest(TYPE_POINT_OF_INTEREST2);

        List<TypePointOfInterest> listTypePointOfInterest = LiveDataTestUtil.getValue(this.realEstateDatabase.typePointOfInterestDao().getTypePointOfInterest());
        assertEquals(2, listTypePointOfInterest.size());
        int TYPE_POINT_OF_INTEREST_ID2 = 2;
        assertEquals(listTypePointOfInterest.get(1).getIdTypePointOfInterest(), TYPE_POINT_OF_INTEREST_ID2);
    }

    // TEST point_of_interest TABLE
    private static String NAME_POINT_OF_INTEREST1 = "Athénée Royal de Waterloo";
    private static PointOfInterest POINT_OF_INTEREST1 = new PointOfInterest(TYPE_POINT_OF_INTEREST_ID1, "", NAME_POINT_OF_INTEREST1);
    private static int POINT_OF_INTEREST_ID1 = 1;

    @Test
    public void insertAndGetPointOfInterest() throws InterruptedException {
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        this.realEstateDatabase.typePointOfInterestDao().insertTypePointOfInterest(TYPE_POINT_OF_INTEREST1);
        this.realEstateDatabase.pointOfInterestDao().insertPointOfInterest(POINT_OF_INTEREST1);

        List<PointOfInterest> pointOfInterest = LiveDataTestUtil.getValue(this.realEstateDatabase.pointOfInterestDao().getPointOfInterest());
        assertEquals(1, pointOfInterest.size());
        assertEquals(pointOfInterest.get(0).getIdPointOfInterest(), POINT_OF_INTEREST_ID1);
    }

    // TEST house_types TABLE
    private static HouseType HOUSE_TYPE1 = new HouseType("Mansion");
    private static HouseType HOUSE_TYPE2 = new HouseType("Building");

    private static int HOUSE_TYPE_ID1 = 1;

    @Test
    public void insertAndGetHouseTypes(){
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE1);
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE2);

        List<HouseType> houseTypeList = this.realEstateDatabase.houseTypeDao().getHouseTypes();
        assertEquals(2, houseTypeList.size());
        assertEquals(houseTypeList.get(0).getIdHouseType(), HOUSE_TYPE_ID1);
        int HOUSE_TYPE_ID2 = 2;
        assertEquals(houseTypeList.get(1).getIdHouseType(), HOUSE_TYPE_ID2);
    }

    // TEST real_estate_agent TABLE
    private static RealEstateAgent REAL_ESTATE_AGENT_1 = new RealEstateAgent("de Meeûs d'Argenteuil", "Augustin");
    private static RealEstateAgent REAL_ESTATE_AGENT_2 = new RealEstateAgent("Morrey", "Rowena");

    private static int REAL_ESTATE_AGENT_ID1 = 1;

    @Test
    public void insertAndGetRealEstateAgent(){
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_1);
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_2);

        List<RealEstateAgent> realEstateAgents = this.realEstateDatabase.realEstateAgentDao().getRealEstateAgent();
        assertEquals(2, realEstateAgents.size());
        assertEquals(realEstateAgents.get(0).getIdRealEstateAgent(), REAL_ESTATE_AGENT_ID1);
        int REAL_ESTATE_AGENT_ID2 = 2;
        assertEquals(realEstateAgents.get(1).getIdRealEstateAgent(), REAL_ESTATE_AGENT_ID2);
    }

    // TEST room TABLE
    private static com.openclassrooms.realestatemanager.models.pojo.Room ROOM_1 = new com.openclassrooms.realestatemanager.models.pojo.Room("bathroom");
    private static com.openclassrooms.realestatemanager.models.pojo.Room ROOM_2 = new com.openclassrooms.realestatemanager.models.pojo.Room("kitchen");

    private static int ROOM_ID1 = 1;
    private static int ROOM_ID2 = 2;

    @Test
    public void insertAndGetRoom(){
        this.realEstateDatabase.roomDao().insertRoom(ROOM_1);
        this.realEstateDatabase.roomDao().insertRoom(ROOM_2);

        List<com.openclassrooms.realestatemanager.models.pojo.Room> roomList = this.realEstateDatabase.roomDao().getRooms();
        assertEquals(2, roomList.size());
        assertEquals(roomList.get(0).getIdRoom(), ROOM_ID1);
        assertEquals(roomList.get(1).getIdRoom(), ROOM_ID2);
    }

    // TEST house TABLE
    private static House HOUSE_1 = new House(HOUSE_TYPE_ID1, REAL_ESTATE_AGENT_ID1, 150000, 150, "Petite maison",
            "Available", 150000);
    private static int HOUSE_ID1 = 1;

    @Test
    public void insertAndGetHouse() throws InterruptedException {
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE1);
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_1);
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        this.realEstateDatabase.houseDao().insertHouse(HOUSE_1);

        List<House> housesList = LiveDataTestUtil.getValue(this.realEstateDatabase.houseDao().getHouses());
        assertEquals(1, housesList.size());
        assertEquals(housesList.get(0).getIdHouse(), HOUSE_ID1);
    }

    // TEST room_numbers TABLE
    private static RoomNumber ROOM_NUMBER_1 = new RoomNumber(HOUSE_ID1, ROOM_ID1, 4);
    private static RoomNumber ROOM_NUMBER_2 = new RoomNumber(HOUSE_ID1, ROOM_ID2, 1);

    @Test
    public void insertAndGetRoomNumber() throws InterruptedException {
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE1);
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_1);
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        this.realEstateDatabase.houseDao().insertHouse(HOUSE_1);
        this.realEstateDatabase.roomDao().insertRoom(ROOM_1);
        this.realEstateDatabase.roomDao().insertRoom(ROOM_2);
        this.realEstateDatabase.roomNumberDao().insertRoomNumber(ROOM_NUMBER_1);
        this.realEstateDatabase.roomNumberDao().insertRoomNumber(ROOM_NUMBER_2);

        List<RoomNumber> roomNumberList = LiveDataTestUtil.getValue(this.realEstateDatabase.roomNumberDao().getRoomNumber());
        assertEquals(2, roomNumberList.size());
        assertEquals(roomNumberList.get(0).getIdHouse(), HOUSE_ID1);
        assertEquals(roomNumberList.get(1).getIdHouse(), HOUSE_ID1);
        assertEquals(roomNumberList.get(1).getIdRoom(), ROOM_ID2);
    }

    // TEST photos TABLE
    private static Photo PHOTO_1 = new Photo();
    private static Photo PHOTO_2 = new Photo();

    @Test
    public void insertAndGetPhotos(){
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE1);
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_1);
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        this.realEstateDatabase.houseDao().insertHouse(HOUSE_1);
        this.realEstateDatabase.roomDao().insertRoom(ROOM_1);
        this.realEstateDatabase.roomDao().insertRoom(ROOM_2);
        this.realEstateDatabase.roomNumberDao().insertRoomNumber(ROOM_NUMBER_1);
        this.realEstateDatabase.roomNumberDao().insertRoomNumber(ROOM_NUMBER_2);
        this.realEstateDatabase.photoDao().insertPhotos(PHOTO_1);
        this.realEstateDatabase.photoDao().insertPhotos(PHOTO_2);

        List<Photo> photoList = this.realEstateDatabase.photoDao().getPhotos();
        assertEquals(2, photoList.size());
    }

    // TEST house_point_of_interest TABLE
    private static HousePointOfInterest HOUSE_POINT_OF_INTEREST_1 = new HousePointOfInterest(HOUSE_ID1, POINT_OF_INTEREST_ID1);

    @Test
    public void insertAndGetHousePointOfInterest() throws InterruptedException {
        this.realEstateDatabase.houseTypeDao().insertHouseType(HOUSE_TYPE1);
        this.realEstateDatabase.realEstateAgentDao().insertRealEstateAgent(REAL_ESTATE_AGENT_1);
        this.realEstateDatabase.addressDao().insertAddress(ADDRESS1);
        this.realEstateDatabase.houseDao().insertHouse(HOUSE_1);
        this.realEstateDatabase.typePointOfInterestDao().insertTypePointOfInterest(TYPE_POINT_OF_INTEREST1);
        this.realEstateDatabase.pointOfInterestDao().insertPointOfInterest(POINT_OF_INTEREST1);
        this.realEstateDatabase.housePointOfInterestDao().insertHousePointOfInterest(HOUSE_POINT_OF_INTEREST_1);

        List<HousePointOfInterest> housePointOfInterestList = LiveDataTestUtil.getValue(this.realEstateDatabase.housePointOfInterestDao().getHousePointOfInterest());
        assertEquals(1, housePointOfInterestList.size());
        assertEquals(housePointOfInterestList.get(0).getIdHouse(), HOUSE_ID1);
        assertEquals(housePointOfInterestList.get(0).getIdPointOfInterest(), POINT_OF_INTEREST_ID1);
    }

    @After
    public void closeDb(){
        realEstateDatabase.close();
    }
}