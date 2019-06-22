package com.example.triviaquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.triviaquiz.QuizContract;

import java.util.ArrayList;

/*
 *  TODO: https://www.youtube.com/watch?v=pEDVdSUuWXE <----- next video
 * */

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    //super constructor auto generated
    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        //JAVA's shit way of generating an sql table
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContract.QuestionsTable.TABLE_NAME + " ( " +
                QuizContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_ANSWER_NUMBER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    //only call onUpgrade if app is in production. s
    /* STEPS:
     * define changes in SQL_CREATE_QUESTIONS_TABLE
     * call db.execSQL("DROP TABLE IF EXISTS" + QuestionsTable.TABLE_NAME
     * call onCreate(db)
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*Create questions initially*/
    private void fillQuestionsTable() {
        //TODO: get rid of dummy data
        Question q1 = new Question("A is correct", "A", "B", "C", "D", 1);
        addQuestion(q1);
        Question q2 = new Question("B is correct", "A", "B", "C", "D", 1);
        addQuestion(q2);
        Question q3 = new Question("C is correct", "A", "B", "C", "D", 1);
        addQuestion(q3);
        Question q4 = new Question("D is correct", "A", "B", "C", "D", 1);
        addQuestion(q4);

    }

    //add new questions
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        //param 1 = what table you want to enter valeus into, param 2 = question object field
        cv.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NUMBER, question.getAnswerNr());

        db.insert(QuizContract.QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionsList = new ArrayList<>();
        db = getReadableDatabase(); //reference to database to grab data out of. when method is called the first time, it calles onCreateDatabasee method
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME, null); //cursor allows queries
        /*
         * Queries first entry, then moves onto next entry if it exists
         * */
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NUMBER)));
                questionsList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionsList;
    }
}
