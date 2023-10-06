package com.example.test;

import android.widget.ImageView;

public class GameObject {
        protected ImageView m_Texture;
        protected float m_PosX;
        protected float m_PosY;

        protected float m_MoveX;
        protected float m_MoveY;

        public void SetMove(float x, float y)
        {
            m_MoveX = x;
            m_MoveY = y;
        }

}
