package com.example.test;
import android.app.Activity;
import android.content.Intent;

import com.example.test.GameObject;

public class GallLine extends GameObject
{
    GallLine(float screeneheight)
    {
        m_PosX = 0.0f;
        m_PosY = screeneheight / 6 * 5;
    }

    public void checkGall(MainActivity main, GallLine gall, Enemy[] enemy)
    {
        for (int i = 0; i < enemy.length; i++)
        {
            if (gall.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && gall.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && enemy[i].m_PosX < gall.m_PosX + gall.m_Texture.getWidth())
            {
                //ゴール処理
            }
        }

    }


}
