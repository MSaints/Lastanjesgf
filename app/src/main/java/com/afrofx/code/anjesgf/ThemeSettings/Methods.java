package com.afrofx.code.anjesgf.ThemeSettings;

import com.afrofx.code.anjesgf.R;

public class Methods {


    public void setColorTheme(){

        switch (Constant.color){
            case 0xFFFF5722:
                Constant.theme = R.style.AppTheme_orange;
                break;
            case 0xFFEA3438:
                Constant.theme = R.style.AppTheme_pink;
                break;
            case 0xFF3F51B5:
                Constant.theme = R.style.AppTheme_yellow;
                break;
            case 0xFF212121:
                Constant.theme = R.style.AppTheme_grey;
                break;
            case 0xFF03A9F4:
                Constant.theme = R.style.AppTheme_blue;
                break;
            case 0xFF009688:
                Constant.theme = R.style.AppTheme_teal;
                break;
            case 0xFF4CAF50:
                Constant.theme = R.style.AppTheme_green;
                break;
            case 0xff795548:
                Constant.theme = R.style.AppTheme_brown;
                break;
            default:
                Constant.theme = R.style.AppTheme;
                break;
        }
    }

}
