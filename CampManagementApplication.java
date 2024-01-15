package camp;

import camp.model.Score;
import camp.model.Student;
import camp.model.Subject;

import java.util.*;

public class CampManagementApplication {
    // 데이터 저장소
    private static List<Student> studentStore; //학생목록
    private static List<Subject> subjectStore; //과목 목록
    private static List<Score> scoreStore; //점수 목록
    private static HashMap<String, Map<String, ArrayList<Score>>> scoreMap = new HashMap<>();
    //1st String = studentId
    //1st Map = 해당 학생의 과목 Map <과목아이디 , 해당과목 점수리스트>
    private static final HashMap<String, ArrayList<Subject>> subjectList = new HashMap<>(); // 학생 한명 이 갖게 될 과목 목록
    //String = studentId
    //ArrayList<subject> 학생이 가진 과목 목록

    // 과목 타입
    private static String SUBJECT_TYPE_MANDATORY = "MANDATORY";
    private static String SUBJECT_TYPE_CHOICE = "CHOICE";

    // index 관리 필드
    private static int studentIndex;
    private static final String INDEX_TYPE_STUDENT = "ST";
    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";
    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    // 스캐너
    public static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        setInitData();
        try {
            displayMainView();
        } catch (Exception e) {
            System.out.println("\n오류 발생!\n프로그램을 종료합니다.");
        }
    }

    // 초기 데이터 생성
    private static void setInitData() {
        studentStore = new ArrayList<>(); // 학생 목록 생성
        subjectStore = List.of( //과목 목록 생성 -> 번호, 이름, 타입
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Java",
                        SUBJECT_TYPE_MANDATORY
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "객체지향",
                        SUBJECT_TYPE_MANDATORY
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Spring",
                        SUBJECT_TYPE_MANDATORY
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "JPA",
                        SUBJECT_TYPE_MANDATORY
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "MySQL",
                        SUBJECT_TYPE_MANDATORY
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "디자인 패턴",
                        SUBJECT_TYPE_CHOICE
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Spring Security",
                        SUBJECT_TYPE_CHOICE
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Redis",
                        SUBJECT_TYPE_CHOICE
                ),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "MongoDB",
                        SUBJECT_TYPE_CHOICE
                )
        );
        scoreStore = new ArrayList<>(); // 점수 보관함 생성
    }

    // index 자동 증가
    private static String sequence(String type) { //String ID 생성기. 1부터 시작. Todo : 0부터 시작하는 건 어떤지?
        switch (type) {
            case INDEX_TYPE_STUDENT -> {
                studentIndex++;
                return INDEX_TYPE_STUDENT + studentIndex; //ST1 , ST2
            }
            case INDEX_TYPE_SUBJECT -> {
                subjectIndex++;
                return INDEX_TYPE_SUBJECT + subjectIndex;
            }
            default -> {
                scoreIndex++;
                return INDEX_TYPE_SCORE + scoreIndex;
            }
        }
    }

    private static void displayMainView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("\n==================================");
            System.out.println("내일배움캠프 수강생 관리 프로그램 실행 중...");
            System.out.println("1. 수강생 관리");
            System.out.println("2. 점수 관리");
            System.out.println("3. 프로그램 종료");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();
            sc.nextLine();


            switch (input) {
                case 1 -> displayStudentView(); // 수강생 관리
                case 2 -> displayScoreView(); // 점수 관리
                case 3 -> flag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(2000);
                }
            }
        }
        System.out.println("프로그램을 종료합니다.");
    }

    private static void displayStudentView() {
        boolean flag = true;
        while (flag) {
            System.out.println("==================================");
            System.out.println("수강생 관리 실행 중...");
            System.out.println("1. 수강생 등록");
            System.out.println("2. 수강생 목록 조회");
            System.out.println("3. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();
            sc.nextLine();//버퍼 비우기

            switch (input) {
                case 1 -> createStudent(); // 수강생 등록
                case 2 -> inquireStudent(); // 수강생 목록 조회
                case 3 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    // 수강생 등록
    private static void createStudent() {
        System.out.println("\n수강생을 등록합니다...");
        System.out.print("수강생 이름 입력:");
        String studentName = sc.nextLine();
        // 기능 구현 (필수 과목, 선택 과목)
        Student student = new Student(sequence(INDEX_TYPE_STUDENT), studentName); // 수강생 인스턴스 생성 예시 코드
        // 기능 구현
        studentStore.add(student);
        createSubjectList(student);
        System.out.println("수강생 등록 성공!\n");
    }

    //과목 입력
    public static void createSubjectList(Student student) {
        String input;
        String[] temp;
        String id = student.getStudentId();

        System.out.println("수강과목 입력(쉼표로 구분, enter시 종료)");
        input = sc.nextLine();
        System.out.println("입력완료");
//        System.out.println(input);
        temp = input.split(",");
//        System.out.println(Arrays.toString(temp)); //출력확인
        checkSubjectList(id, temp, student); //과목중복체크
    }

    public static Subject findSubject(String name) throws NullPointerException {
        Subject res = null;
        for (int i = 0; i < subjectStore.size(); i++) {
            if (subjectStore.get(i).getSubjectName().equals(name)) {
                res = subjectStore.get(i);
            }
        }
        if (res == null) {
            System.out.println("과목 값 없음..." + subjectStore.size());
        }
        return res;
    }

    public static void checkSubjectList(String id, String[] subjects, Student student) {
        int essentialCount = 0;
        int optionalCount = 0;
        int check = 0;
        ArrayList<Subject> temp = new ArrayList<>();

        for (String subjectName : subjects) {
            Subject subject = findSubject(subjectName);
            temp.add(subject);
            if (SUBJECT_TYPE_MANDATORY.equals(subject.getSubjectType())) {
                essentialCount++;
            } else if (SUBJECT_TYPE_CHOICE.equals(subject.getSubjectType())) {
                optionalCount++;
            }
        }

        if (essentialCount <= 3 && optionalCount <= 2) {
            System.out.println("선택한 필수 과목이 3개 미만입니다!");
            System.out.println("선택한 선택 과목이 2개 미만입니다!");
            check = 1;
        } else if (essentialCount <= 3) {
            System.out.println("선택한 필수 과목이 3개 미만입니다!");
            check = 1;
        } else if (optionalCount <= 2) {
            System.out.println("선택한 선택 과목이 2개 미만입니다!");
            check = 1;
        } else {
            subjectList.put(id, temp); // subject list에 추가
            System.out.println("과목이 정상적으로 입력되었습니다.");
        }

        if (check == 1) {
            System.out.println("과목을 다시 입력해주세요");
            createSubjectList(student);
        }

        try {
            Thread.sleep(2000); // 3초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 수강생 목록 조회
    private static void inquireStudent() {
        System.out.println("\n수강생 목록을 조회합니다...");
        // 기능 구현
        System.out.println("----------------------------");
        for (Student s : studentStore) {
            System.out.println("ID | " + s.getStudentId() + "      이름 | " + s.getStudentName());
        }
        System.out.println("----------------------------");
        System.out.println("\n수강생 목록 조회 성공!");
    }

    private static void displayScoreView() {
        boolean flag = true;
        while (flag) {
            System.out.println("==================================");
            System.out.println("점수 관리 실행 중...");
            System.out.println("1. 수강생의 과목별 시험 회차 및 점수 등록");
            System.out.println("2. 수강생의 과목별 회차 점수 수정");
            System.out.println("3. 수강생의 특정 과목 회차별 등급 조회");
            System.out.println("4. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();
            sc.nextLine();

            switch (input) {
                case 1 -> createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case 2 -> updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    private static String getStudentId() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        return "ST" + sc.nextLine();
    }

    // 수강생의 과목별 시험 회차 및 점수 등록
    private static void createScore() {
        System.out.print("시험 점수를 등록합니다...");
        inquireStudent();//학생목록 조회
        boolean judge;
        String studentId;
        do {
            studentId = getStudentId();
            if (subjectList.containsKey(studentId)) {
                displayStudentSubjectList(studentId);
                judge = false;
            } else {
                System.out.println("해당 ID를 가진 학생은 존재하지 않습니다. 다시 입력해주세요.");
                judge = true;
            }
        } while (judge);

        //Step2. 과목 선택하는 입력
        System.out.println("등록하고자하는 '과목 번호'를 입력하세요.(입력예 : 1)");
        int subjectIndex;
        do {
            subjectIndex = sc.nextInt() - 1;
            sc.nextLine(); // 버퍼지우기
            if (subjectIndex >= subjectList.get(studentId).size() || subjectIndex < 0) {
                System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
                judge = true;
            } else {
                judge = false;
            }
        } while (judge);

        String inputSubjectName = subjectList.get(studentId).get(subjectIndex).getSubjectName(); // 등록하고자 하는 과목명을 가져옴
        if (scoreMap.containsKey(studentId)) {
            //학생의 아이디 가지고 있는 경우
            if (scoreMap.get(studentId).containsKey(inputSubjectName)) {//해당 과목을 가지고 있는 경우
//                System.out.println("case 1");
                int roundInfo = scoreMap.get(studentId).get(inputSubjectName).size() + 1; //해당 과목 점수 리스트에 추가
                System.out.println(inputSubjectName + " " + roundInfo + " 회차 입력중입니다.");
            } else { // 해당과목에 대한 정보가 없는경우
//                System.out.println("case 2");
                System.out.println(inputSubjectName + " 1회차 입력중입니다.");
            }
        } else {
//            System.out.println("case 3");
            //학생에 대한 성적 입력이 처음인 경우
            System.out.println(inputSubjectName + " 1회차 입력중입니다.");
        }
        System.out.println("점수를 입력해주세요...");
        System.out.println("(단, 점수는 0~100 의 값)");
        int inputScore;
        do {
            inputScore = sc.nextInt();
            sc.nextLine();//버퍼지우기
            if (inputScore > 100 || inputScore < 0) {
                System.out.println("잘못된 점수 입니다. 다시 입력해주세요.");
                judge = true;
            } else {
                judge = false;
            }
        } while (judge);

        if (scoreMap.containsKey(studentId)) {
            //학생의 아이디 가지고 있는 경우
            if (scoreMap.get(studentId).containsKey(inputSubjectName)) {//해당 과목을 가지고 있는 경우
//                System.out.println("case 1");
                scoreMap.get(studentId).get(inputSubjectName).add(new Score(inputScore)); //해당 과목 점수 리스트에 추가
            } else { // 해당과목에 대한 정보가 없는경우
//                System.out.println("case 2");
                ArrayList<Score> scoreList = new ArrayList<>();
                scoreList.add(new Score(inputScore));
                HashMap<String, ArrayList<Score>> subjectScoreMap = new HashMap<>();//해당 과목 맵 생성
                scoreMap.get(studentId).put(inputSubjectName, scoreList);
            }
        } else {
//            System.out.println("case 3");
            //학생에 대한 성적 입력이 처음인 경우
            ArrayList<Score> scoreList = new ArrayList<>();
            scoreList.add(new Score(inputScore));
            HashMap<String, ArrayList<Score>> subjectScoreMap = new HashMap<>();
            subjectScoreMap.put(inputSubjectName, scoreList);
            scoreMap.put(studentId, subjectScoreMap);
        }
        System.out.println("\n점수 등록 성공!");
    }

    private static void displayStudentSubjectList(String studentId) {
        ArrayList<Subject> studentSubjectList = subjectList.get(studentId); // 학생이 가지고 있는 과목 리스트를 studentSubjectList에 저장
        int index = 1;
        System.out.println("----------------------------");
        for (Subject subject : studentSubjectList) {
            System.out.print("[ " + index + ". " + subject.getSubjectName() + " ] ");
            index++;
        }
        System.out.println("\n----------------------------");
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        System.out.println("시험 점수를 수정합니다...");
        inquireStudent(); // 학생 리스트
        boolean judge;
        String studentId;
        do {
            studentId = getStudentId();
            if (subjectList.containsKey(studentId)) {
                displayStudentSubjectList(studentId);
                judge = false;
            } else {
                System.out.println("해당 ID를 가진 학생은 존재하지 않습니다. 다시 입력해주세요.");
                judge = true;
            }
        } while (judge);
        //Step2. 과목 선택하는 입력
        System.out.println("수정하고자하는 '과목 번호'를 입력하세요...");
        int subjectIndex;
        do {
            subjectIndex = sc.nextInt() - 1;
            sc.nextLine(); // 버퍼지우기
            if (subjectIndex >= subjectList.get(studentId).size() || subjectIndex < 0) {
                System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
                judge = true;
            } else {
                judge = false;
            }
        } while (judge);

        String inputSubjectName = subjectList.get(studentId).get(subjectIndex).getSubjectName(); // 수정하고자 하는 과목명
        //검증 과정! 만약 해당과목에 점수가 없는경우에는 "해당과목의 성적이 존재하지 않습니다." 출력
        ArrayList<Score> scoreList;
        if (scoreMap.containsKey(studentId)) {
            //학생의 아이디 가지고 있는 경우
            if (scoreMap.get(studentId).containsKey(inputSubjectName)) {//해당 과목을 가지고 있는 경우
//                System.out.println("case 1");
                scoreList = scoreMap.get(studentId).get(inputSubjectName);
            } else { // 해당과목에 대한 정보가 없는경우
//                System.out.println("case 2");
                System.out.println("해당과목의 성적이 존재하지 않습니다.");
                return;
            }
        } else {
//            System.out.println("case 3");
            //학생에 대한 성적 입력이 처음인 경우
            System.out.println("해당과목의 성적이 존재하지 않습니다.");
            return;
        }

        int index = 1;
        for (Object score : scoreList) {
            System.out.print("[ " + index++ + "회 : " + ((Score) score).getTestScore() + " 점 ]");
        }
        System.out.println("\n수정을 원하는 회차를 입력하세요.(입력예 : 1)");
        int inputRound;
        do {
            inputRound = sc.nextInt() - 1;
            sc.nextLine();
            if (inputRound < 0 || inputRound >= scoreList.size()) {
                System.out.println("잘못된 회차입니다. 다시 입력해주세요.");
                judge = true;
            } else {
                judge = false;
            }
        } while (judge);

        System.out.println("점수를 입력해주세요...");
        System.out.println("(단, 점수는 0~100 의 값)");
        int inputScore;
        do {
            inputScore = sc.nextInt();
            sc.nextLine();//버퍼지우기
            if (inputScore > 100 || inputScore < 0) {
                System.out.println("잘못된 점수 입니다. 다시 입력해주세요.");
                judge = true;
            } else {
                judge = false;
            }
        } while (judge);
        scoreMap.get(studentId).get(inputSubjectName).get(inputRound).setTestScore(inputScore);
//        scoreList.set(inputRound,new Score(inputScore));
        /* 원인
         * 1. 일단 어레이 리스트에서 set함수가 동작하는 방식이 내가 생각한 방식으로 동작하는지 알 수 없다.
         * set함수의 파라미터는 int index , Object object 인데, 여기서 Object 부분을 처리하려면 new Score(inputScore) 이어야할듯*/


        // 기능 구현
        System.out.println("\n점수 수정 성공!");
    }

    // 수강생의 특정 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        inquireStudent();
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        // 기능 구현 (조회할 특정 과목)
        displayStudentSubjectList(studentId);
        //---------임시-------------------------------
        // 점수 등록이 정상적으로 되었는지 확인한 코드입니다. 수정하시면 됩니다.
        System.out.println("조회하고자하는 '과목 번호'를 입력하세요...");
        int subjectIndex = sc.nextInt() - 1;
        sc.nextLine();//버퍼지우기
        if (subjectIndex >= subjectList.get(studentId).size() || subjectIndex < 0) {
            System.out.println("잘못된 번호입니다. 메뉴로 돌아갑니다.");
            return;
        }
        String inputSubjectName = subjectList.get(studentId).get(subjectIndex).getSubjectName(); // 조회하고자하는 과목명
        if (!scoreMap.get(studentId).containsKey(inputSubjectName)) {
            System.out.println("해당과목은 등록된 점수가 없습니다.");
            return;
        }
        System.out.println("\n해당과목 점수를 조회합니다...");
        ArrayList<Score> temp = scoreMap.get(studentId).get(inputSubjectName);
        int index = 1;
        for (Score score : temp) {
            System.out.println(index++ + "회차 : " + score.getTestScore());

        }
        //-------------------------------------------

        System.out.println("회차별 등급을 조회합니다...");
        // 기능 구현
        System.out.println("\n등급 조회 성공!");
    }

}
