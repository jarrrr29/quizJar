package com.example.quizjar; // PASTIKAN INI SESUAI DENGAN NAMA PAKET ANDA

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Tetap ada jika Anda ingin menggunakan Toast untuk pesan akhir kuis

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvAnswerStatus;
    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;
    private Button btnRestartQuiz; // Deklarasi tombol restart baru
    private Button[] optionButtons;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen UI
        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_score);
        tvAnswerStatus = findViewById(R.id.tv_answer_status);

        btnOptionA = findViewById(R.id.btn_option_a);
        btnOptionB = findViewById(R.id.btn_option_b);
        btnOptionC = findViewById(R.id.btn_option_c);
        btnOptionD = findViewById(R.id.btn_option_d);
        btnRestartQuiz = findViewById(R.id.btn_restart_quiz); // Inisialisasi tombol restart

        // Masukkan semua tombol opsi ke dalam array untuk memudahkan iterasi
        optionButtons = new Button[]{btnOptionA, btnOptionB, btnOptionC, btnOptionD};

        // Siapkan soal-soal kuis
        setupQuestions();

        // Sembunyikan tombol restart saat aplikasi dimulai
        btnRestartQuiz.setVisibility(View.GONE);

        // Tampilkan soal pertama
        displayQuestion();
    }

    private void setupQuestions() {
        questionList = new ArrayList<>();

        // Soal 1
        questionList.add(new Question(
                "Pekanbaru dulunya dikenal sebagai 'Senapelan'. Siapakah yang mendirikan Senapelan?",
                new String[]{"Sultan Abdul Jalil Rahmad Syah", "Sultan Siak IV", "Raja Kecik", "Yang Dipertuan Muda Riau"},
                "Sultan Abdul Jalil Rahmad Syah"
        ));

        // Soal 2
        questionList.add(new Question(
                "Pada tanggal berapa Kota Pekanbaru resmi didirikan?",
                new String[]{"23 Juni 1784", "12 Mei 1789", "16 Juli 1800", "28 September 1780"},
                "23 Juni 1784"
        ));

        // Soal 3
        questionList.add(new Question(
                "Sungai yang menjadi urat nadi perdagangan di Pekanbaru sejak dulu adalah Sungai...",
                new String[]{"Sungai Kampar", "Sungai Siak", "Sungai Indragiri", "Sungai Rokan"},
                "Sungai Siak"
        ));

        // Soal 4 (Opsional, untuk menambahkan lebih dari 3 soal)
        questionList.add(new Question(
                "Bangunan bersejarah di Pekanbaru yang dulunya merupakan kediaman Sultan Siak adalah...",
                new String[]{"Balai Adat Melayu Riau", "Istana Siak Sri Indrapura", "Rumah Singgah Tuan Kadi", "Mesjid Raya Pekanbaru"},
                "Rumah Singgah Tuan Kadi"
        ));

        // Acak urutan soal (opsional, agar tidak monoton)
        Collections.shuffle(questionList);
    }

    private void displayQuestion() {
        // Reset status jawaban dan warna tombol
        tvAnswerStatus.setText("");
        resetButtonColors();
        enableButtons(); // Pastikan tombol aktif untuk soal baru

        // Sembunyikan tombol restart setiap kali soal baru ditampilkan
        btnRestartQuiz.setVisibility(View.GONE);

        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            tvQuestion.setText(currentQuestion.getQuestionText());

            // Acak opsi jawaban sebelum ditampilkan
            List<String> options = new ArrayList<>(currentQuestion.getOptions());
            Collections.shuffle(options);

            btnOptionA.setText(options.get(0));
            btnOptionB.setText(options.get(1));
            btnOptionC.setText(options.get(2));
            btnOptionD.setText(options.get(3));

            // Perbarui skor yang ditampilkan
            tvScore.setText("Skor: " + score);
        } else {
            // Kuis selesai
            tvQuestion.setText("Kuis Selesai!");
            tvAnswerStatus.setText("Skor akhir Anda: " + score + "!");
            tvAnswerStatus.setTextColor(Color.BLACK); // Reset warna teks status
            disableButtons(); // Matikan tombol opsi
            btnRestartQuiz.setVisibility(View.VISIBLE); // Tampilkan tombol restart
        }
    }

    // Metode untuk mengecek jawaban saat tombol diklik
    public void checkAnswer(View view) {
        Button clickedButton = (Button) view;
        String selectedAnswer = clickedButton.getText().toString();
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();

        disableButtons(); // Nonaktifkan tombol sementara setelah menjawab

        if (selectedAnswer.equals(correctAnswer)) {
            score += 10; // Tambah skor jika benar (sesuaikan nilai poin)
            tvAnswerStatus.setText("Benar!");
            tvAnswerStatus.setTextColor(Color.GREEN);
            clickedButton.setBackgroundColor(Color.GREEN);
        } else {
            tvAnswerStatus.setText("Salah! Jawaban yang benar adalah: " + correctAnswer);
            tvAnswerStatus.setTextColor(Color.RED);
            clickedButton.setBackgroundColor(Color.RED);

            // Tandai jawaban yang benar dengan warna hijau
            for (Button button : optionButtons) {
                if (button.getText().toString().equals(correctAnswer)) {
                    button.setBackgroundColor(Color.GREEN);
                    break;
                }
            }
        }

        // Perbarui skor yang ditampilkan (langsung di TextView)
        tvScore.setText("Skor: " + score);

        // Tunda penampilan soal berikutnya agar pengguna bisa melihat status jawaban
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                displayQuestion(); // Tampilkan soal berikutnya
            }
        }, 1500); // Tunda selama 1.5 detik (1500 milidetik)
    }

    private void disableButtons() {
        for (Button button : optionButtons) {
            button.setEnabled(false);
        }
    }

    private void enableButtons() {
        for (Button button : optionButtons) {
            button.setEnabled(true);
        }
    }

    private void resetButtonColors() {
        // Reset warna tombol ke warna default (misalnya, warna latar belakang tombol default)
        for (Button button : optionButtons) {
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Contoh warna default
        }
    }

    // Metode baru untuk memulai ulang kuis
    public void restartQuiz(View view) {
        currentQuestionIndex = 0; // Reset indeks soal
        score = 0; // Reset skor
        Collections.shuffle(questionList); // Acak ulang soal untuk pengalaman baru
        btnRestartQuiz.setVisibility(View.GONE); // Sembunyikan tombol restart
        displayQuestion(); // Mulai kuis dari awal
    }
}