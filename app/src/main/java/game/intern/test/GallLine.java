package game.intern.test;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class GallLine extends GameObject
{
    GallLine(float screeneheight)
    {
        m_PosX = 0.0f;
        m_PosY = screeneheight / 20 * 19;
    }

    public boolean checkGall(GallLine gall, ArrayList<Enemy> enemies, CollideEffect collideEffect, Drawable drawable)
    {
        if(!enemies.isEmpty())//リストが空ではない
        {
            for (int i = 0; i < enemies.size(); i++) {
                if (gall.m_PosY + (float)gall.m_Texture.getHeight()/2 < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                        && gall.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                        && enemies.get(i).m_PosX < gall.m_PosX + gall.m_Texture.getWidth()) {

                    collideEffect.collideEffect((int) (enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()/2),
                            (int) (gall.m_PosY),
                            drawable,420,420,5000);
                    //ゴール処理
                    return true;
                }
            }
        }

        return false;
    }




}
