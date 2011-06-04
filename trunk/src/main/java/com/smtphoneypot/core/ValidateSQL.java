package com.smtphoneypot.core;

public class ValidateSQL
{
    public ValidateSQL()
    {
        
    }
    
    // clean a string
    public String clean(String s)
    {
        String str = "";
        for (int i = 0; i < s.length(); i++)
        {
            
            str = str + this.contains(s.charAt(i));
            
        }
        // // remove the single quotes that could cause an SQL injection attack
        // need to remove ' -- ; and #
        return str;
    }
    
    // escape all of the single quotes
    public String contains(char inval)
    {
        char[] a = { '\'' };
        for (int i = 0; i < a.length; i++)
        {
            if (inval == a[i])
            {
                return new String("'" + inval);
            }
        }
        return new String("" + inval);
    }
    
    // clean a where clause string
    public String whereClean(String s)
    {
        String str = "";
        
        // firstly escape the single quotes
        // then escape the percent and underscore
        s = this.clean(s);
        
        for (int i = 0; i < s.length(); i++)
        {
            str = str + this.likeContains(s.charAt(i));
        }
        return str;
    }
    
    // escape the underscore and percent form the where clause in the like
    public String likeContains(char inval)
    {
        char[] b = { '_', '%' };
        for (int i = 0; i < b.length; i++)
        {
            if (inval == b[i])
            {
                return new String("\\" + inval);
            }
        }
        return new String("" + inval);
    }
}
