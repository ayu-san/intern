package game.intern.test;

import java.util.ArrayList;

public class GallLine extends GameObject
{
    GallLine(float screeneheight)
    {
        m_PosX = 0.0f;
        m_PosY = screeneheight / 20 * 19;
    }

    public boolean checkGall(GallLine gall, ArrayList<Enemy> enemies)
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
