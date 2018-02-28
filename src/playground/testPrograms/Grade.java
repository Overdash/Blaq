package playground.testPrograms;

public class Grade {

    private int SID;
    private int CID;
    private int grade;

    public Grade(int sid, int cid, int g){
        SID = sid;
        CID = cid;
        grade = g;
    }

    public int getGrade() {
        return grade;
    }

    public int getCID() {
        return CID;
    }

    public int getSID() {
        return SID;
    }
}
