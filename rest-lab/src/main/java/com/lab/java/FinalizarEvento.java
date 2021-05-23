package com.lab.java;

import java.util.TimerTask;

class FinalizarEvento extends TimerTask{

   public FinalizarEvento(){
     //Some stuffs
   }

   @Override
   public void run() {
     System.out.println("Hi see you after 10 seconds");
   }

}