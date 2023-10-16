package com.example.test;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.test.GameObject;

import java.util.ArrayList;
import com.example.test.MainActivity;

public class GallLine extends GameObject
{
    GallLine(float screeneheight)
    {
        m_PosX = 0.0f;
        m_PosY = screeneheight / 6 * 5;
    }

    public boolean checkGall(MainActivity main, GallLine gall, ArrayList<Enemy> enemies)
    {
        if(!enemies.isEmpty())//リストが空ではない
        {
            for (int i = 0; i < enemies.size(); i++) {
                if (gall.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                        && gall.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                        && enemies.get(i).m_PosX < gall.m_PosX + gall.m_Texture.getWidth()) {
                    //ゴール処理
                    return true;
                }
            }
        }

        return false;
    }




}
