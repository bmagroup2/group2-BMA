package bmasec2.bmaapplication.system;

import bmasec2.bmaapplication.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistenceManager {

    private static final String DATA_DIR = "data";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static <T extends Serializable> void saveObjects(List<T> objects, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + File.separator + filename))) {
            oos.writeObject(objects);
            System.out.println("Objects saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving objects to " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> List<T> loadObjects(String filename) {
        File file = new File(DATA_DIR + File.separator + filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<T> objects = (List<T>) ois.readObject();
            System.out.println("Objects loaded from " + filename);
            return objects;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading objects from " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void addAuditLogAndSave(AuditLog newLog) {
        List<AuditLog> auditLogs = loadObjects("audit_logs.dat");
        auditLogs.add(newLog);
        saveObjects(auditLogs, "audit_logs.dat");
    }

    public static void addReportAndSave(Report newReport) {
        List<Report> reports = loadObjects("reports.dat");
        reports.add(newReport);
        saveObjects(reports, "reports.dat");
    }

    public static void addSystemSettingAndSave(SystemSettings newSetting) {
        List<SystemSettings> systemSettings = loadObjects("system_settings.dat");
        systemSettings.add(newSetting);
        saveObjects(systemSettings, "system_settings.dat");
    }

    public static void addAnnouncementAndSave(Announcement newAnnouncement) {
        List<Announcement> announcements = loadObjects("announcements.dat");
        announcements.add(newAnnouncement);
        saveObjects(announcements, "announcements.dat");
    }

    public static void addEvaluationAndSave(Evaluation newEvaluation) {
        List<Evaluation> evaluations = loadObjects("evaluations.dat");
        evaluations.add(newEvaluation);
        saveObjects(evaluations, "evaluations.dat");
    }

    public static void addInstructorAndSave(Instructor newInstructor) {
        List<Instructor> instructors = loadObjects("instructors.dat");
        instructors.add(newInstructor);
        saveObjects(instructors, "instructors.dat");
    }

    public static void addCadetRankingAndSave(CadetRanking newRanking) {
        List<CadetRanking> cadetRankings = loadObjects("cadet_rankings.dat");
        cadetRankings.add(newRanking);
        saveObjects(cadetRankings, "cadet_rankings.dat");
    }

    public static void addMissionEventAndSave(MissionEvent newEvent) {
        List<MissionEvent> missionEvents = loadObjects("mission_events.dat");
        missionEvents.add(newEvent);
        saveObjects(missionEvents, "mission_events.dat");
    }

    public static void addLeaveAndSave(Leave newRequest) {
        List<Leave> leaveRequests = loadObjects("leave_requests.dat");
        leaveRequests.add(newRequest);
        saveObjects(leaveRequests, "leave_requests.dat");
    }

    public static void addAttendanceAndSave(Attendance newAttendance) {
        List<Attendance> attendances = loadObjects("attendance.dat");
        attendances.add(newAttendance);
        saveObjects(attendances, "attendance.dat");
    }

    public static void addInventoryItemAndSave(InventoryItem newItem) {
        List<InventoryItem> inventoryItems = loadObjects("inventory_items.dat");
        inventoryItems.add(newItem);
        saveObjects(inventoryItems, "inventory_items.dat");
    }

    public static void addMedicalRecordAndSave(MedicalRecord newRecord) {
        List<MedicalRecord> medicalRecords = loadObjects("medical_records.dat");
        medicalRecords.add(newRecord);
        saveObjects(medicalRecords, "medical_records.dat");
    }

    public static void addMenuAndSave(bmasec2.bmaapplication.model.Menu newMenu) {
        List<bmasec2.bmaapplication.model.Menu> menus = loadObjects("meal_menus.dat");
        menus.add(newMenu);
        saveObjects(menus, "meal_menus.dat");
    }

    public static void addFoodStockAndSave(FoodStock newStock) {
        List<FoodStock> foodStocks = loadObjects("food_stocks.dat");
        foodStocks.add(newStock);
        saveObjects(foodStocks, "food_stocks.dat");
    }

    public static void addMealAttendanceAndSave(MealAttendance newAttendance) {
        List<MealAttendance> mealAttendances = loadObjects("meal_attendances.dat");
        mealAttendances.add(newAttendance);
        saveObjects(mealAttendances, "meal_attendances.dat");
    }

    public static void addVaccinationRecordAndSave(VaccinationRecord newRecord) {
        List<VaccinationRecord> vaccinationRecords = loadObjects("vaccination_records.dat");
        vaccinationRecords.add(newRecord);
        saveObjects(vaccinationRecords, "vaccination_records.dat");
    }

    public static void addSpecialMealRequestAndSave(SpecialMealRequest newRequest) {
        List<SpecialMealRequest> mealRequests = loadObjects("special_meal_requests.dat");
        mealRequests.add(newRequest);
        saveObjects(mealRequests, "special_meal_requests.dat");
    }
}


