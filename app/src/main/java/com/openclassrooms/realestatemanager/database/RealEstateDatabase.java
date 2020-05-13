package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.database.dao.HouseDao;
import com.openclassrooms.realestatemanager.database.dao.HousePointOfInterestDao;
import com.openclassrooms.realestatemanager.database.dao.HouseTypeDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateAgentDao;
import com.openclassrooms.realestatemanager.database.dao.RoomDao;
import com.openclassrooms.realestatemanager.database.dao.RoomNumberDao;
import com.openclassrooms.realestatemanager.database.dao.TypePointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

@Database(entities = {Address.class,
        House.class,
        HousePointOfInterest.class,
        HouseType.class,
        Photo.class,
        PointOfInterest.class,
        RealEstateAgent.class,
        Room.class,
        RoomNumber.class,
        TypePointOfInterest.class},
        version = 2, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase {

    // --- SINGLETON ---
    // Volatile keyword ensure that the variable is always up to date even if it's use by two different thread
    private static volatile RealEstateDatabase INSTANCE;

    // --- DAO ---
    public abstract AddressDao addressDao();
    public abstract HouseDao houseDao();
    public abstract HousePointOfInterestDao housePointOfInterestDao();
    public abstract HouseTypeDao houseTypeDao();
    public abstract PhotoDao photoDao();
    public abstract PointOfInterestDao pointOfInterestDao();
    public abstract RealEstateAgentDao realEstateAgentDao();
    public abstract RoomDao roomDao();
    public abstract RoomNumberDao roomNumberDao();
    public abstract TypePointOfInterestDao typePointOfInterestDao();


    // --- INSTANCE ---
    public static RealEstateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateDatabase.class, "RealEstateDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues houseType1 = new ContentValues();
                houseType1.put("house_type", "Mansion");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType1);

                ContentValues houseType2 = new ContentValues();
                houseType2.put("house_type", "Building");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType2);

                ContentValues houseType3 = new ContentValues();
                houseType3.put("house_type", "House");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType3);

                ContentValues houseType4 = new ContentValues();
                houseType4.put("house_type", "Appartement");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType4);

                ContentValues houseType5 = new ContentValues();
                houseType5.put("house_type", "Penthouse");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType5);

                ContentValues houseType6 = new ContentValues();
                houseType6.put("house_type", "Duplex");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType6);

                ContentValues houseType7 = new ContentValues();
                houseType7.put("house_type", "Flat");
                db.insert("house_types", OnConflictStrategy.IGNORE, houseType7);

                ContentValues roomType1 = new ContentValues();
                roomType1.put("room_type", "Kitchen");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType1);

                ContentValues roomType2 = new ContentValues();
                roomType2.put("room_type", "Bathroom");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType2);

                ContentValues roomType3 = new ContentValues();
                roomType3.put("room_type", "Bedroom");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType3);

                ContentValues roomType4 = new ContentValues();
                roomType4.put("room_type", "Living room");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType4);

                ContentValues roomType5 = new ContentValues();
                roomType5.put("room_type", "Toilet");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType5);

                ContentValues roomType6 = new ContentValues();
                roomType6.put("room_type", "Cellar");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType6);

                ContentValues roomType7 = new ContentValues();
                roomType7.put("room_type", "Pool");
                db.insert("rooms", OnConflictStrategy.IGNORE, roomType7);

                ContentValues realEstateAgent1 = new ContentValues();
                realEstateAgent1.put("name", "de Meeûs d'Argenteuil");
                realEstateAgent1.put("firstname", "Augustin");
                db.insert("real_estate_agents", OnConflictStrategy.IGNORE, realEstateAgent1);

                ContentValues realEstateAgent2 = new ContentValues();
                realEstateAgent2.put("name", "Morrey");
                realEstateAgent2.put("firstname", "Rowena");
                db.insert("real_estate_agents", OnConflictStrategy.IGNORE, realEstateAgent2);

                ContentValues realEstateAgent3 = new ContentValues();
                realEstateAgent3.put("name", "Detiège");
                realEstateAgent3.put("firstname", "Sophie");
                db.insert("real_estate_agents", OnConflictStrategy.IGNORE, realEstateAgent3);
            }
        };
    }
}