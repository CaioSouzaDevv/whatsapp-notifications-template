
public class StoreNotificationData {

    private String storeId;
    private String storeName;
    private String coordinatorName;
    private String phone;
    private String goalValue;
    private String currentValue;
    private String referenceDate;
    private String message;

    public StoreNotificationData(
            String storeId,
            String storeName,
            String coordinatorName,
            String phone,
            String goalValue,
            String currentValue,
            String referenceDate,
            String message) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.coordinatorName = coordinatorName;
        this.phone = phone;
        this.goalValue = goalValue;
        this.currentValue = currentValue;
        this.referenceDate = referenceDate;
        this.message = message;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGoalValue() {
        return goalValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public String getMessage() {
        return message;
    }
}