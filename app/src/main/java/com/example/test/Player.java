package com.example.test;
import com.example.test.GameObject;

public class Player extends GameObject
{
    float m_InitialSpeed = 700.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_holdValue;
    int m_ChargeLevel = 0;//チャージ速度
}
