public class SoundInfo implements Comparable<SoundInfo> {

    private String key;

    private String category;

    public SoundInfo(String key, String category) {
        this.key = key;
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(SoundInfo o) {
        if (o == null) {
            return 1;
        }
        int result = this.key.compareTo(o.key);
        if (result == 0) {
            result = this.category.compareTo(o.category);
        }
        return result;
    }
}
