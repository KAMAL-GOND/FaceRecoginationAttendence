package com.example.facerecoginationattendence.Data.LocalDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertStudent(student : Students )
    @Update
    suspend fun UpdateStudent(student: Students)
    @Delete
    suspend fun DeleteStudent(student: Students)

    @Query("select * from Students")
    suspend fun GetALlStudent():List<Students>

    @Query("select * from Students where StudentID = :id")
    suspend fun GetStudent(id: Long): Students

    @Query("select * from Students where Class = :Class")
    suspend fun GetStudentByClass(Class: String): List<Students>

    @Query("select * from Students where name = :name")
    suspend fun GetStudentByName(name: String): List<Students>








}

@Dao
interface ClassDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertClass(Class : Class)
    @Delete
    suspend fun DeleteClass(Class: Class)
    @Query("select * from Class")
    suspend fun GetAllClass(): List<Class>
}

@Dao

interface AttendanceDao {

    // ---------------------------
    // INSERT
    // ---------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(att: Attendence)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAttendance(list: List<Attendence>)


    // ---------------------------
    // UPDATE
    // ---------------------------
    @Update
    suspend fun updateAttendance(att: Attendence)


    // ---------------------------
    // DELETE
    // ---------------------------
    @Delete
    suspend fun deleteAttendance(att: Attendence)

    @Query("DELETE FROM Attendence")
    suspend fun deleteAllAttendance()


    // ---------------------------
    // QUERY: BY ID
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE AttendenceID = :id LIMIT 1")
    suspend fun getAttendanceById(id: Long): Attendence?


    // ---------------------------
    // QUERY: BY STUDENT
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE StudentID = :studentId")
    suspend fun getAttendanceForStudent(studentId: Long): List<Attendence>


    // ---------------------------
    // QUERY: BY CLASS
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE ClassID = :classId")
    suspend fun getAttendanceForClass(classId: Long): List<Attendence>


    // ---------------------------
    // QUERY: BY DATE
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE Date = :date")
    suspend fun getAttendanceByDate(date: String): List<Attendence>


    // ---------------------------
    // QUERY: BY CLASS + DATE
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE ClassID = :classId AND Date = :date")
    suspend fun getClassAttendanceOnDate(classId: Long, date: String): List<Attendence>


    // ---------------------------
    // QUERY: BY STUDENT + DATE
    // ---------------------------
    @Query("SELECT * FROM Attendence WHERE StudentID = :studentId AND Date = :date")
    suspend fun getStudentAttendanceOnDate(studentId: Long, date: String): Attendence?

    // ---------- QUERY: Whole Month ----------
    // Example input → studentId = 1, year = 2025, month = 1
    // This will match "2025-01-%"
    @Query("""
        SELECT * FROM Attendence
        WHERE StudentID = :studentId 
        AND Date LIKE :yearMonth || '%'
        ORDER BY Date ASC
    """)
    suspend fun getMonthlyAttendance(
        studentId: Long,
        yearMonth: String   // "2025-01"
    ): List<Attendence>


    // ---------- QUERY: Date Range ----------
    @Query("""
        SELECT * FROM Attendence
        WHERE StudentID = :studentId 
        AND Date BETWEEN :startDate AND :endDate
        ORDER BY Date ASC
    """)
    suspend fun getAttendanceBetweenDates(
        studentId: Long,
        startDate: String,   // "2025-01-01"
        endDate: String      // "2025-01-31"
    ): List<Attendence>


    // ---------- QUERY: Check If Already Exists ----------
    // Useful to prevent duplicate entries for same student/day
    @Query("""
        SELECT COUNT(*) FROM Attendence
        WHERE StudentID = :studentId 
        AND ClassID = :classId 
        AND Date = :date
    """)
    suspend fun alreadyMarked(
        studentId: Long,
        classId: Long,
        date: String
    ): Int


    // ---------- QUERY: All Attendance For A Class On A Day ----------
    @Query("""
        SELECT * FROM Attendence
        WHERE ClassID = :classId 
        AND Date = :date
    """)
    suspend fun getClassAttendanceForDay(classId: Long, date: String): List<Attendence>
}
