#include "mytext.h"
// 按照二级布莱叶盲文编译盲文
// zhangtairan
// 2023.1.7

//开头为a的单词判断,对应索引可能为1（a）,27（as）,28(and)
//返回索引值
int afunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 1;
        return bitstream;
    }
    else if(str[i + 1] == 'n' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'd')
        {
            if(str[i + 3] == ' ')
            {
                bitstream = 28;
                i += 2;
                return bitstream;
            }
        }
        bitstream = 1;
        return bitstream;
    }
    else if(str[i + 1] == 's' && str[i - 1] == ' ')
    {
        bitstream = 27;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 1;
        return bitstream;
    }
}
//开头为b的单词判断,对应索引可能为2（b，but）
//返回索引值
int bfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 2;
        return bitstream;
    }
    else if(str[i + 1] == 'u' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 't')
        {
            if(str[i + 3] == ' ')
            {
                bitstream = 2;
                i += 2;
                return bitstream;
            }
        }

        bitstream = 2;
        return bitstream;
    }
    else
    {
        bitstream = 2;
        return bitstream;
    }
}
//开头为c的单词判断,对应索引可能为3（c，can）,29（ch）
//返回索引值
int cfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 3;
        return bitstream;
    }
    else if(str[i + 1] == 'a' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'n')
        {
            if(str[i + 3] == ' ')
            {
                bitstream = 3;
                i += 2;
                return bitstream;
            }
        }

        bitstream = 3;
        return bitstream;
    }
    else if(str[i + 1] == 'h')
    {
        bitstream = 32;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 3;
        return bitstream;
    }
}
//开头为d的单词判断,对应索引可能为4（d，do）
//返回索引值
int dfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 4;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 4;
            i += 1;
            return bitstream;
        }

        bitstream = 4;
        return bitstream; 
    }
    else
    {
        bitstream = 4;
        return bitstream;
    }
}
//开头为e的单词判断,对应索引可能为5（e，every）,37（ed）,38（er）
//返回索引值
int efunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 5;
        return bitstream;
    }
    else if(str[i + 1] == 'v' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'e')
        {
            if(str[i + 3] == 'r')
            {
                if(str[i + 4] == 'y')
                {
                    if(str[i + 5] == ' ')
                    {
                        bitstream = 5;
                        i += 4;
                        return bitstream;
                    }
                } 
            }
            bitstream = 5;
            return bitstream;
        }
        else
        {
            bitstream = 5;
            return bitstream;
        }
    }
    else if(str[i + 1] == 'd')
    {
        bitstream = 37;
        i += 1;
        return bitstream;
    }
    else if(str[i + 1] == 'r')
    {
        bitstream = 38;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 3;
        return bitstream;
    }
}
//开头为f的单词判断,对应索引可能为6（f，from）, 28 (for)
//返回索引值
int ffunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 6;
        return bitstream;
    }
    else if(str[i + 1] == 'r' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'o')
        {
            if(str[i + 3] == 'm')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 6;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 6;
        return bitstream; 
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'r')
        {
            if(str[i + 4] == ' ')
            {
                bitstream = 28;
                i += 2;
                return bitstream;
            }
        }
        bitstream = 6;
        return bitstream; 
    }
    else
    {
        bitstream = 6;
        return bitstream;
    }
}
//开头为g的单词判断,对应索引可能为7（g，go）,33（gh）
//返回索引值
int gfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 7;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 7;
            i += 1;
            return bitstream; 
        }
        bitstream = 7;
        return bitstream; 
    }
    else if(str[i + 1] == 'h')
    {
        bitstream = 33;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 7;
        return bitstream;
    }
}
//开头为h的单词判断,对应索引可能为8（h，have）
//返回索引值
int hfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 8;
        return bitstream;
    }
    else if(str[i + 1] == 'a' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'v')
        {
            if(str[i + 3] == 'e')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 8;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 8;
        return bitstream; 
    }
    else
    {
        bitstream = 8;
        return bitstream;
    }
}
//开头为h的单词判断,对应索引可能为10（j,just）
//返回索引值
int jfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 10;
        return bitstream;
    }
    else if(str[i + 1] == 'u' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 's')
        {
            if(str[i + 3] == 't')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 10;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 10;
        return bitstream; 
    }
    else
    {
        bitstream = 10;
        return bitstream;
    }
}
//开头为h的单词判断,对应索引可能为11（k，knowledge）
//返回索引值
int kfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 11;
        return bitstream;
    }
    else if(str[i + 1] == 'n' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'o')
        {
            if(str[i + 3] == 'w')
            {
                if(str[i + 4] == 'l')
                {
                    if(str[i + 5] == 'e')
                    {
                        if(str[i + 6] == 'd')
                        {
                            if(str[i + 7] == 'g')
                            {
                                if(str[i + 8] == 'e')
                                {
                                    bitstream = 11;
                                    i += 8;
                                    return bitstream;
                                }
                            }
                        }
                    }
                }
            } 
        }
        bitstream = 11;
        return bitstream; 
    }
    else
    {
        bitstream = 11;
        return bitstream;
    }
}
//开头为l的单词判断,对应索引可能为12（l,like）
//返回索引值
int lfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 12;
        return bitstream;
    }
    else if(str[i + 1] == 'i' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'k')
        {
            if(str[i + 3] == 'e')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 12;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 12;
        return bitstream; 
    }
    else
    {
        bitstream = 12;
        return bitstream;
    }
}
//开头为m的单词判断,对应索引可能为13（m,more）
//返回索引值
int mfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 13;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'r')
        {
            if(str[i + 3] == 'e')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 13;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 13;
        return bitstream; 
    }
    else
    {
        bitstream = 13;
        return bitstream;
    }
}
//开头为n的单词判断,对应索引可能为14（n,not）
//返回索引值
int nfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 14;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 't')
        {
            if(str[i + 3] == ' ')
            {
                bitstream = 14;
                i += 2;
                return bitstream;
            } 
        }
        bitstream = 14;
        return bitstream; 
    }
    else
    {
        bitstream = 14;
        return bitstream;
    }
}
//开头为o的单词判断,对应索引可能为15(o),29(of),39(ou),40(ow)
//返回索引值
int ofunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 15;
        return bitstream;
    }
    else if(str[i + 1] == 'f' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 29;
            i += 1;
            return bitstream;
        } 

        bitstream = 15;
        return bitstream; 
    }
    else if(str[i + 1] == 'u')
    {
        bitstream = 39;
        i += 1;
        return bitstream;
    }
    else if(str[i + 1] == 'w')
    {
        bitstream = 40;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 15;
        return bitstream;
    }
}
//开头为p的单词判断,对应索引可能为16（p,people）
//返回索引值
int pfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 16;
        return bitstream;
    }
    else if(str[i + 1] == 'e' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'o')
        {
            if(str[i + 3] == 'p')
            {
                if(str[i + 4] == 'l')
                {
                    if(str[i + 5] == 'e')
                    {
                        if(str[i + 6] == ' ')
                        {
                            bitstream = 16;
                            i += 5;
                            return bitstream;
                        }
                    }
                }
            } 
        }
        bitstream = 16;
        return bitstream; 
    }
    else
    {
        bitstream = 16;
        return bitstream;
    }
}
//开头为q的单词判断,对应索引可能为17（q,quite）
//返回索引值
int qfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 17;
        return bitstream;
    }
    else if(str[i + 1] == 'u' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'i')
        {
            if(str[i + 3] == 't')
            {
                if(str[i + 4] == 'e')
                {
                    if(str[i + 5] == ' ')
                    {
                        bitstream = 17;
                        i += 4;
                        return bitstream;
                    }
                }
            } 
        }
        bitstream = 17;
        return bitstream; 
    }
    else
    {
        bitstream = 17;
        return bitstream;
    }
}
//开头为r的单词判断,对应索引可能为18（r,rather）
//返回索引值
int rfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 18;
        return bitstream;
    }
    else if(str[i + 1] == 'a' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 't')
        {
            if(str[i + 3] == 'h')
            {
                if(str[i + 4] == 'e')
                {
                    if(str[i + 5] == 'r')
                    {
                        if(str[i + 6] == ' ')
                        {
                            bitstream = 18;
                            i += 5;
                            return bitstream;
                        }
                    }
                }
            } 
        }
        bitstream = 18;
        return bitstream; 
    }
    else
    {
        bitstream = 18;
        return bitstream;
    }
}
//开头为s的单词判断,对应索引可能为19（s，so）,34(sh)
//返回索引值
int sfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 19;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 19;
            i += 1;
            return bitstream;
        }
        bitstream = 19;
        return bitstream; 
    }
    else if(str[i + 1] == 'h')
    {
        bitstream = 34;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 19;
        return bitstream;
    }
}
//开头为t的单词判断,对应索引可能为20（t,that）,35(th), 30(the)
//返回索引值
int tfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 20;
        return bitstream;
    }
    else if(str[i + 1] == 'h' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'a')
        {
            if(str[i + 3] == 't')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 20;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 20;
        return bitstream; 
    }
    else if(str[i + 1] == 'h' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'e')
        {
            if(str[i + 4] == ' ')
            {
                bitstream = 30;
                i += 2;
                return bitstream;
            }
        }
        bitstream = 20;
        return bitstream; 
    }
    else if(str[i + 1] == 'h')
    {
        bitstream = 35;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 20;
        return bitstream;
    }
}
//开头为u的单词判断,对应索引可能为21（u，us）
//返回索引值
int ufunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 21;
        return bitstream;
    }
    else if(str[i + 1] == 's' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 21;
            i += 1;
            return bitstream;
        }
        bitstream = 21;
        return bitstream; 
    }
    else
    {
        bitstream = 21;
        return bitstream;
    }
}
//开头为v的单词判断,对应索引可能为22（v,very）
//返回索引值
int vfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 22;
        return bitstream;
    }
    else if(str[i + 1] == 'e' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'r')
        {
            if(str[i + 3] == 'y')
            {
                if(str[i + 4] == ' ')
                {
                    bitstream = 22;
                    i += 3;
                    return bitstream;
                }
            } 
        }
        bitstream = 22;
        return bitstream; 
    }
    else
    {
        bitstream = 22;
        return bitstream;
    }
}
//开头为w的单词判断,对应索引可能为23（w,will）,36(wh),31(with)
//返回索引值
int wfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 23;
        return bitstream;
    }
    else if(str[i + 1] == 'i' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'l')
        {
            if(str[i + 3] == 'l')
            {
                if(str[i + 3] == 'l')
                {
                    bitstream = 23;
                    i += 3;
                    return bitstream;
                }
            }
        }
        else if(str[i + 2] == 't')
        {
            if(str[i + 3] == 'l')
            {
                if(str[i + 3] == 'l')
                {
                    bitstream = 31;
                    i += 3;
                    return bitstream;
                }
            }
        }
        bitstream = 23;
        return bitstream; 
    }
    else if(str[i + 1] == 'h')
    {

        bitstream = 36;
        i += 1;
        return bitstream;
    }
    else
    {
        bitstream = 23;
        return bitstream;
    }
}
//开头为i的单词判断,对应索引可能为9（i), 23(it)
//返回索引值
int ifunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 9;
        return bitstream;
    }
    else if(str[i + 1] == 't' && str[i - 1] == ' ')
    {
        if(str[i + 2] == ' ')
        {
            bitstream = 23;
            i += 1;
            return bitstream;
        }
        bitstream = 9;
        return bitstream; 
    }
    else
    {
        bitstream = 9;
        return bitstream;
    }
}
//开头为y的单词判断,对应索引可能为25（y，you）
//返回索引值
int yfunc(char *str, int &i)
{
    int bitstream = 0;
    if(str[i + 1] == ' ')
    {
        bitstream = 25;
        return bitstream;
    }
    else if(str[i + 1] == 'o' && str[i - 1] == ' ')
    {
        if(str[i + 2] == 'u')
        {
            if(str[i + 3] == ' ')
            {
                bitstream = 25;
                i += 2;
                return bitstream;
            }
        }
        bitstream = 25;
        return bitstream; 
    }
    else
    {
        bitstream = 25;
        return bitstream;
    }
}
bool isNum(char c)
{
    if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' 
        || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
    {
        return true;
    }
    return false;
}
//字符序列到索引数组的变化
//输入字符序列及其长度
//返回索引数组
char *alphabet2byte(char* str, int size)
{
    int i = 0;
    int j = 0;
    int *Num = new int[500];
    int bitstream = 0;
    while(i < size)
    {
        //判断a
        if(str[i] == 'a')
        {
            bitstream = afunc(str, i);
            Num[j] = bitstream;
        }
        //判断b
        else if(str[i] == 'b')
        {
            bitstream = bfunc(str, i);
            Num[j] = bitstream;
        }
        //判断c
        else if(str[i] == 'c')
        {
            bitstream = cfunc(str, i);
            Num[j] = bitstream;
        }
        //判断d
        else if(str[i] == 'd')
        {
            bitstream = dfunc(str, i);
            Num[j] = bitstream;
        }
        //判断e
        else if(str[i] == 'e')
        {
            bitstream = efunc(str, i);
            Num[j] = bitstream;
        }
        //判断f
        else if(str[i] == 'f')
        {
            bitstream = ffunc(str, i);
            Num[j] = bitstream;
        }
        //判断g
        else if(str[i] == 'g')
        {
            bitstream = gfunc(str, i);
            Num[j] = bitstream;
        }
        //判断h
        else if(str[i] == 'h')
        {
            bitstream = hfunc(str, i);
            Num[j] = bitstream;
        }
        //判断i
        else if(str[i] == 'i')
        {
            bitstream = ifunc(str, i);
            Num[j] = bitstream;
        }
        //判断g
        else if(str[i] == 'j')
        {
            bitstream = jfunc(str, i);
            Num[j] = bitstream;
        }
        //判断k
        else if(str[i] == 'k')
        {
            bitstream = kfunc(str, i);
            Num[j] = bitstream;
        }
        //判断l
        else if(str[i] == 'l')
        {
            bitstream = lfunc(str, i);
            Num[j] = bitstream;
        }
        //判断m
        else if(str[i] == 'm')
        {
            bitstream = mfunc(str, i);
            Num[j] = bitstream;
        }
        //判断n
        else if(str[i] == 'n')
        {
            bitstream = nfunc(str, i);
            Num[j] = bitstream;
        }
        //判断o
        else if(str[i] == 'o')
        {
            bitstream = ofunc(str, i);
            Num[j] = bitstream;
        }
        //判断p
        else if(str[i] == 'p')
        {
            bitstream = pfunc(str, i);
            Num[j] = bitstream;
        }
        //判断q
        else if(str[i] == 'q')
        {
            bitstream = qfunc(str, i);
            Num[j] = bitstream;
        }
        //判断r
        else if(str[i] == 'r')
        {
            bitstream = rfunc(str, i);
            Num[j] = bitstream;
        }
        //判断s
        else if(str[i] == 's')
        {
            bitstream = sfunc(str, i);
            Num[j] = bitstream;
        }
        //判断d
        else if(str[i] == 't')
        {
            bitstream = tfunc(str, i);
            Num[j] = bitstream;
        }
        //判断u
        else if(str[i] == 'u')
        {
            bitstream = ufunc(str, i);
            Num[j] = bitstream;
        }
        //判断v
        else if(str[i] == 'v')
        {
            bitstream = vfunc(str, i);
            Num[j] = bitstream;
        }
        //判断w
        else if(str[i] == 'w')
        {
            bitstream = wfunc(str, i);
            Num[j] = bitstream;
        }
        //判断x
        else if(str[i] == 'x')
        {
            bitstream = 24;
            Num[j] = bitstream;
        }
        //判断y
        else if(str[i] == 'y')
        {
            bitstream = yfunc(str, i);
            Num[j] = bitstream;
        }
        //判断z
        else if(str[i] == 'z')
        {
            bitstream = 26;
            Num[j] = bitstream;
        }
        //判断,
        else if(str[i] == ',')
        {
            bitstream = 41;
            Num[j] = bitstream;
        }
        //判断;
        else if(str[i] == ';')
        {
            bitstream = 42;
            Num[j] = bitstream;
        }
        //判断:
        else if(str[i] == ':')
        {
            bitstream = 43;
            Num[j] = bitstream;
        }
        //判断.
        else if(str[i] == '.')
        {
            bitstream = 44;
            Num[j] = bitstream;
        }
        //判断!
        else if(str[i] == '!')
        {
            bitstream = 46;
            Num[j] = bitstream;
        }
        //判断数字
        else if(isNum(str[i]))
        {
            if(i == 0 || !isNum(str[i - 1]))
            {
                bitstream = 53;       //数字判断
                Num[j] = bitstream;
                j++;
            }
            
            if(str[i] == '1')
            {
                bitstream = 1;
                Num[j] = bitstream;
            }
            else if(str[i] == '2')
            {
                bitstream = 2;
                Num[j] = bitstream;
            }
            else if(str[i] == '3')
            {
                bitstream = 3;
                Num[j] = bitstream;
            }
            else if(str[i] == '4')
            {
                bitstream = 4;
                Num[j] = bitstream;
            }
            else if(str[i] == '5')
            {
                bitstream = 5;
                Num[j] = bitstream;
            }
            else if(str[i] == '6')
            {
                bitstream = 6;
                Num[j] = bitstream;
            }
            else if(str[i] == '7')
            {
                bitstream = 7;
                Num[j] = bitstream;
            }
            else if(str[i] == '8')
            {
                bitstream = 8;
                Num[j] = bitstream;
            }
            else if(str[i] == '9')
            {
                bitstream = 9;
                Num[j] = bitstream;
            }
            else
            {
                bitstream = 10;
                Num[j] = bitstream;
            }

            if(i == size - 1 || !isNum(str[i + 1]))
            {
                bitstream = 53;       //数字判断
                j++;
                Num[j] = bitstream;
            }
            
        }
        //判断空格
        else if(str[i] == ' ')
        {
            bitstream = 0;
            Num[j] = bitstream;
        }
        i++;
        j++;
    }
    Num[j] = 65;
    char *a = new char[100];
    for(int k = 0; k <= j; k++)
    {
        if(Num[k] <= 31)
        {
            a[k] = Num[k] + 66;
        }
        else
        {
            a[k] = Num[k];
        }
    }
    a[j] = 0;
    return a;
}
// //字符串预处理
// int* text(char *s, int size)
// {
//     int *num_text = alphabet2byte(s, size);
//     return num_text;
// }

// int main()
// {
//     int i = 0;
//     string s = "124";
//     string s1 = " " + s + " ";
//     char *a;
//     a = (char *)s1.data();
//     int *b;
//     b = alphabet2byte(a, s1.length());
//     printf("Enter\n");
//     while(b[i] != 65)
//     {
//         printf("%d ", b[i]);
//         i++;
//     }
//     delete b;
// }

