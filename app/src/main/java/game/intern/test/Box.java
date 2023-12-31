package game.intern.test;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Box extends GameObject
{
    Box(ImageView texture,float posX, float posY)
    {
        m_Texture = texture;
        m_PosX = posX;
        m_PosY = posY;

        m_Texture.setX(m_PosX);
        m_Texture.setY(m_PosY);
    }
    public boolean CollisionwithPlayer(Player player, Box boxes, CollideEffect effect, Drawable drawable)
    {
        long durations = 500;
        
        {
//            // 下面
//            if (player.m_PosY + (int)((float)player.m_Texture.getHeight() / 3.0f)  < boxes.m_PosY + boxes.m_Texture.getHeight()
//                    && player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.0f) < boxes.m_PosX + boxes.m_Texture.getWidth()
//                    && boxes.m_PosX < player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.0f)
//                    && boxes.m_PosY + boxes.m_Texture.getHeight() < player.m_oldPosY + (int)((float)player.m_Texture.getHeight() / 3.0f))
//            {
//                player.m_PosY = boxes.m_PosY + boxes.m_Texture.getHeight();
//                effect.collideEffect((int) player.m_PosX + player.m_Texture.getWidth() / 2, (int) (boxes.m_PosY + boxes.m_Texture.getHeight()), drawable, 350, 350, durations);
//
//                player.m_MoveY *= -1.0;
//                player.m_MoveVecY *= -1.0;
//                return true;
//            }
//
//            // ボックスの上面との当たり判定
//            if (player.m_PosY + (player.m_Texture.getHeight() / 4.0f * 3.0f) > boxes.m_PosY
//                    && player.m_PosY + player.m_Texture.getHeight() <= boxes.m_PosY + boxes.m_Texture.getHeight()
//                    && player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.0f) < boxes.m_PosX + boxes.m_Texture.getWidth()
//                    && boxes.m_PosX < player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.0f)
//                    && player.m_oldPosY + (player.m_Texture.getHeight() / 4.0f * 3.0f) <= boxes.m_PosY)
//            {
//                player.m_PosY = boxes.m_PosY - player.m_Texture.getHeight();
//                effect.collideEffect((int) (player.m_PosX + player.m_Texture.getWidth() / 2), (int) (boxes.m_PosY), drawable, 350, 350, durations);
//
//                player.m_MoveY *= -1.0;
//                player.m_MoveVecY *= -1.0;
//                return true;
//            }
//
//            //右面
//            if(player.m_PosX + (int)((float)player.m_Texture.getWidth() / 4.0f)  < boxes.m_PosX + boxes.m_Texture.getWidth()
//                    && player.m_PosY + (int)((float)player.m_Texture.getHeight() / 2.0f)  < boxes.m_PosY + (boxes.m_Texture.getHeight())
//                    && boxes.m_PosY < player.m_PosY + (int)((float)player.m_Texture.getHeight() / 2.0f)
//                    && boxes.m_PosX + boxes.m_Texture.getWidth() < player.m_oldPosX + (int)((float)player.m_Texture.getWidth() / 4.0f))
//            {
//                {
//                    player.m_PosX = boxes.m_PosX + boxes.m_Texture.getWidth();
//                    effect.collideEffect((int) (boxes.m_PosX + boxes.m_Texture.getWidth()), (int) (player.m_PosY + player.m_Texture.getHeight() /2), drawable, 350, 350, durations);
//
//                    player.m_MoveX *= -1.0;
//                    player.m_MoveVecX *= -1.0;
//                    return true;
//                }
//            }
//
//            //左面
//            if (boxes.m_PosX < player.m_PosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 4.0f)
//                    && player.m_PosY + (int)((float)player.m_Texture.getHeight() / 2.0f)  < boxes.m_PosY + (boxes.m_Texture.getHeight())
//                    && boxes.m_PosY < player.m_PosY + (int)((float)player.m_Texture.getHeight() / 2.0f)
//                    && player.m_oldPosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 4.0f) < boxes.m_PosX)
//            {
//                player.m_PosX = boxes.m_PosX - player.m_Texture.getWidth();
//                effect.collideEffect((int) (boxes.m_PosX), (int) (player.m_PosY + player.m_Texture.getHeight() / 2), drawable, 350, 350, durations);
//
//                player.m_MoveX *= -1.0;
//                player.m_MoveVecX *= -1.0;
//                return true;
//            }

            // 下面
            if (player.m_PosY + (int)((float)player.m_Texture.getHeight() / 3.0f)  < boxes.m_PosY + boxes.m_Texture.getHeight()
                    && player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.5f) < boxes.m_PosX + boxes.m_Texture.getWidth()
                    && boxes.m_PosX < player.m_PosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 2.5f)
                    && boxes.m_PosY + boxes.m_Texture.getHeight() < player.m_oldPosY + (int)((float)player.m_Texture.getHeight() / 3.0f))
            {
                player.m_PosY = boxes.m_PosY + boxes.m_Texture.getHeight();
                effect.collideEffect((int) player.m_PosX + player.m_Texture.getWidth() / 2, (int) (boxes.m_PosY + boxes.m_Texture.getHeight()), drawable, 350, 350, durations);

                player.m_MoveY *= -1.0;
                player.m_MoveVecY *= -1.0;
                return true;
            }

            // ボックスの上面との当たり判定
            if (player.m_PosY + (player.m_Texture.getHeight() / 4.0f * 3.0f) > boxes.m_PosY
                    && player.m_PosY + player.m_Texture.getHeight() <= boxes.m_PosY + boxes.m_Texture.getHeight()
                    && player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.5f) < boxes.m_PosX + boxes.m_Texture.getWidth()
                    && boxes.m_PosX < player.m_PosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 2.5f)
                    && player.m_oldPosY + (player.m_Texture.getHeight() / 4.0f * 3.0f) <= boxes.m_PosY)
            {
                player.m_PosY = boxes.m_PosY - player.m_Texture.getHeight();
                effect.collideEffect((int) (player.m_PosX + player.m_Texture.getWidth() / 2), (int) (boxes.m_PosY), drawable, 350, 350, durations);

                player.m_MoveY *= -1.0;
                player.m_MoveVecY *= -1.0;
                return true;
            }

            //左面
            if(player.m_PosX + (int)((float)player.m_Texture.getWidth() / 2.0f)  < boxes.m_PosX + boxes.m_Texture.getWidth()
                    && player.m_PosY  < boxes.m_PosY + (boxes.m_Texture.getHeight() / 5.0f * 2.5f)
                    && boxes.m_PosY + (boxes.m_Texture.getHeight() / 5.0f * 2.5f)  < player.m_PosY + player.m_Texture.getHeight()
                    && boxes.m_PosX + boxes.m_Texture.getWidth() < player.m_oldPosX + (int)((float)player.m_Texture.getWidth() / 2.0f))
            {
                {
                    player.m_PosX = boxes.m_PosX + boxes.m_Texture.getWidth();
                    effect.collideEffect((int) (boxes.m_PosX + boxes.m_Texture.getWidth()), (int) (player.m_PosY + player.m_Texture.getHeight() /2), drawable, 350, 350, durations);

                    player.m_MoveX *= -1.0;
                    player.m_MoveVecX *= -1.0;
                    return true;
                }
            }

            //右面
            if (boxes.m_PosX < player.m_PosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 3.0f)
                    && player.m_PosY < boxes.m_PosY + (boxes.m_Texture.getHeight() / 5.0f * 2.5f)
                    && boxes.m_PosY + (boxes.m_Texture.getHeight() / 5.0f * 2.5f)  < player.m_PosY + player.m_Texture.getHeight()
                    && player.m_oldPosX + player.m_Texture.getWidth() - (int)((float)player.m_Texture.getWidth() / 3.0f) < boxes.m_PosX)
            {
                player.m_PosX = boxes.m_PosX - player.m_Texture.getWidth();
                effect.collideEffect((int) (boxes.m_PosX), (int) (player.m_PosY + player.m_Texture.getHeight() / 2), drawable, 350, 350, durations);

                player.m_MoveX *= -1.0;
                player.m_MoveVecX *= -1.0;
                return true;
            }
            return false;
        }


    }


}
