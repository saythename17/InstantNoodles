package com.icandothisallday.instantnoodles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class InstantNoodlesActivity extends AppCompatActivity {
    final int WATER=500;
    int temperature=15;
    boolean complete=false;
    boolean isEgg=false;
    boolean allInPot=false;


    ImageView pot,water,waterDrop,soup,sari,greenOnion,egg,eggDrop,question,no;
    LinearLayout waterTemp;
    TextView elapsedTime,currentTemp,timerText;
    SeekBar tempProgress;
    CountDownTimer timer;
    long baseTime;
    int seconds, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_noodles);

        AlertDialog.Builder builder= new AlertDialog.Builder(InstantNoodlesActivity.this,
                R.style.MyDialog);
        builder.setIcon(R.drawable.ic);
        builder.setTitle("\n라면 주문이 접수 되었어!");
        builder.setMessage("\n안내에 따라 조리해 주면 라면이 완성돼.\n" +
                "먼저 물을 냄비에 넣어줘.\n라면에 들어갈 재료들은 길게 터치후\n냄비로 드래그해서 넣을 수 있어." );
        builder.setPositiveButton("시작", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseTime=SystemClock.elapsedRealtime();
                elapsedTimer.sendEmptyMessage(0);
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();

        elapsedTime =findViewById(R.id.time);
        timerText=findViewById(R.id.timer);
        pot=findViewById(R.id.pot);
        water=findViewById(R.id.water);
        waterDrop=findViewById(R.id.water_drop);
        waterTemp =findViewById(R.id.progress_water_temp);
        currentTemp=findViewById(R.id.current_temp);
        tempProgress=findViewById(R.id.current_temp_progress);
        soup=findViewById(R.id.powder);
        sari=findViewById(R.id.sari);
        greenOnion=findViewById(R.id.greenOnion);
        egg=findViewById(R.id.egg);
        eggDrop=findViewById(R.id.egg_drop);
        question=findViewById(R.id.q);
        no=findViewById(R.id.no);

        timer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("남은 시간 : "+millisUntilFinished/1000 + " 초");
            }

            @Override
            public void onFinish() {
                elapsedTimer.removeMessages(0);
                Intent intent = new Intent(getBaseContext(),CompleteActivity.class);
                startActivity(intent);
            }
        };

        water.setTag("WATER IMG");
        soup.setTag("SOUP IMG");
        sari.setTag("SARI IMG");
        greenOnion.setTag("ONION IMG");
        egg.setTag("EGG IMG");
        water.setOnLongClickListener(longClickListener);
        soup.setOnLongClickListener(longClickListener);
        sari.setOnLongClickListener(longClickListener);
        greenOnion.setOnLongClickListener(longClickListener);
        egg.setOnLongClickListener(longClickListener);
        pot.setOnDragListener(dragListener);
        pot.setOnClickListener(potClick);
    }

    Handler elapsedTimer = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            elapsedTimer.sendEmptyMessage(0);
            long now=SystemClock.elapsedRealtime();
            elapsedTime.setText(timeFormat(now-baseTime));

            if(minutes!=0 && seconds %60==0){
                Animation animation= AnimationUtils.loadAnimation(getBaseContext(),
                        R.anim.alpha);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation animation2= AnimationUtils.loadAnimation(getBaseContext(),
                                R.anim.only_alpha);
                        animation2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                question.setVisibility(View.INVISIBLE);
                                no.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        no.setVisibility(View.VISIBLE);
                        no.setAnimation(animation2);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                question.setVisibility(View.VISIBLE);
                question.setAnimation(animation);
            }
        }
    };

    String timeFormat(long time){
        int start_sec = (int) (time/1000);
        minutes = start_sec/60;
        seconds = start_sec%60;
        long millis = (time/10)%100;
        return String.format("%02d:%02d:%02d",minutes, seconds,millis);
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(v.equals(egg)) isEgg=true;
            if(v.equals(sari)) allInPot =true;
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(v.getTag().toString(),mimeTypes,item);
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDrag(data,shadow,v,0);
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action){
                case  DragEvent.ACTION_DRAG_STARTED:
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        return  true;
                    }
                    return  false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return  true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    return true;

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = item.getText().toString();
                    Animation animation= AnimationUtils.loadAnimation(InstantNoodlesActivity.this,
                            R.anim.drop);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(temperature<20){
                                tempProgress.setProgress(temperature);
                                currentTemp.setText("현재온도 : "+temperature+"˚");
                                waterTemp.setVisibility(View.VISIBLE);
                                Toast.makeText(getBaseContext(),"물이 끓을 수 있도록\n냄비를 클릭해서 온도를 높여줘",Toast.LENGTH_LONG).show();
                            }
                            waterDrop.setVisibility(View.INVISIBLE);
                            eggDrop.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    if(temperature<20){
                        waterDrop.setAnimation(animation);
                        waterDrop.setVisibility(View.VISIBLE);
                    }
                    if(isEgg){
                        eggDrop.setVisibility(View.VISIBLE);
                        eggDrop.setAnimation(animation);
                        isEgg=false;
                    }
                    if(allInPot){
                        timerText.setVisibility(View.VISIBLE);
                        timer.start();
                    }

                    return true;

                case  DragEvent.ACTION_DRAG_ENDED:
                    return true;
            }
            return false;
        }
    };

    View.OnClickListener potClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(temperature==100){
                waterTemp.setVisibility(View.GONE);
                pot.setImageResource(R.drawable.boiling);
                Toast.makeText(getBaseContext(),"좋아. 이제 재료들을 모두 넣고 2분만 더 끓이면 라면이 완성 돼!",Toast.LENGTH_LONG).show();
                soup.setVisibility(View.VISIBLE);
                sari.setVisibility(View.VISIBLE);
                greenOnion.setVisibility(View.VISIBLE);
                egg.setVisibility(View.VISIBLE);
            }
            temperature++;
            tempProgress.setProgress(temperature);
            currentTemp.setText("현재온도 : "+temperature+"˚");
        }
    };
}
