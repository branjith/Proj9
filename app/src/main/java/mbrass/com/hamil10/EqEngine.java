package mbrass.com.hamil10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class EqEngine {

    String[] operators ={"+","-","x"};
    java.util.Random random= new java.util.Random();
    int rsign;
    int diff_lvl=1; /* 1,2,3 */
    int value = 0;
    String eqn ="";
    String work="";
    String space="";
    int mod=2;
    static double pi=3.14;

    public EqEngine(int x){
        diff_lvl=2+x;
        int mod1=mod;
        for(int i=0;i<diff_lvl;i++)
        {
            space+=" ";
            mod*=mod1;
        }
    }

    public static void main(String[] args){
        EqEngine ob = new EqEngine(1);
        System.out.println("dyn eqn");
        ob.dynamicEngine(2).toStr();
        System.out.println("q eqn");
        ob.quadraticEqn().toStr();
		/*
		System.out.println("area circle");
		ob.areaCircle().toStr();
		System.out.println("area square");
		ob.areaSquare().toStr();
		System.out.println("area pentagon");
		ob.areaPentagon().toStr();
		System.out.println("area hexagon");
		ob.areaHexagon().toStr();
		System.out.println("circumferance circle");
		ob.circumfernceCircle().toStr();
		System.out.println("volume sphere");
		ob.volumeSphere().toStr();
		System.out.println("volume cube");
		ob.volumeCube().toStr();
		System.out.println("volume cylinder");
		ob.volumeCylinder().toStr();
		System.out.println("volume cone");
		ob.volumeCone().toStr();
		*/
    }


    public M10 nextQuestion(){
        int dif = MainActivity.diff_level;
        int randomQsn = (java.lang.Math.abs(random.nextInt()))%10;
        M10 qsn = null;
        switch(randomQsn){
            //case 0:qsn = getLineareqn();break;
            case 1:qsn = dynamicEngine(dif);break;
            case 2:qsn = quadraticEqn();break;
            case 3:qsn = areaCircle();break;
            case 4:qsn = areaSquare();break;
            case 5:qsn = areaPentagon();break;
            case 6:qsn = areaHexagon();break;
            case 7:qsn = circumfernceCircle();break;
            case 8:qsn = volumeSphere();break;
            case 9:qsn = volumeCube();break;
            case 0:qsn = volumeCylinder();break;
            case 10:qsn = volumeCone();break;
        }
        qsn.meqn=addPadding(qsn.meqn,30);
        return qsn;
    }

    public String replaceSymbols(String qsn){
        HashMap<String,String> symbols = new HashMap<>();
        symbols.put("#sqr#","<sup>2</sup>");
        return null;
    }


    public String addPadding(String qsn,int length){
        int x = (length*2)-qsn.length();
        int y = x/2;
        String pad="";
        for(int i=0;i<y;i++){
            pad+=" ";
        }
        return (pad+qsn+pad).replace("#sqr#","<sup>2</sup>");
    }

    public HashMap<String, String> nextQuestion(String tmp){
        eqnEngine(MainActivity.diff_level+3);
        HashMap<String,String> retStr = new HashMap<String,String>();
        retStr.put("QSN",eqn);
        retStr.put("WORK",work);
        retStr.put("VALUE",String.valueOf(value));
        HashSet<Integer> option=options();
        Iterator it=option.iterator();
        String rstr="";
        while(it.hasNext()){
            int x=(Integer)it.next();
            rstr+=x+" ";
        }
        retStr.put("OPTION",rstr);
        reset();
        return retStr;
    }

    public void eqEngineRunner(){

        eqnEngine(diff_lvl);
        System.out.println(eqn+"="+value);
        System.out.println(work);
        HashSet<Integer> option=options();
        Iterator it=option.iterator();
        System.out.println("++++++++++++++++++++++");
        while(it.hasNext()){
            int x=(Integer)it.next();
            String pre="";
            if(x==value)
                pre="<--";
            System.out.println(x+pre);
        }
        System.out.println("++++++++++++++++++++++");
    }

    public M10 getLineareqn(){
        M10 m10 = new M10();
        eqnEngine(MainActivity.diff_level+3);
        m10.meqn=eqn;
        m10.mvalue=value;
        m10.mwork=work;
        HashSet<Integer> opts = options(value);
        Iterator it = opts.iterator();
        String[] opt = {"","","",""};
        int i=0;
        while(it.hasNext())
            opt[i++]=(String)it.next();

        return m10;
    }

    public HashSet<Integer> options(){
        return options(value);
    }

    public HashSet<Integer> options(int val){
        int limit=10+(java.lang.Math.abs(random.nextInt()))%90,rnum=0;
        HashSet<Integer> option = new HashSet<Integer>();
        ArrayList<Integer> al=new ArrayList<Integer>();
        rnum=1+java.lang.Math.abs(random.nextInt())%limit;
        al.add(val+rnum);
        al.add(val-rnum+20);
        al.add(val*(rnum/10));
        al.add(val-(2*rnum));
        al.add((2*val)+rnum);
        al.add(3*rnum+val);
        al.add(5*rnum+val);
        option.add(val);
        Collections.shuffle(al);
        for(int i=0;i<al.size();i++){
            if(al.get(i)==val)
                option.add(al.get(i+1));
            else
                option.add(al.get(i));
            if(option.size()==4)
                i=100;
        }
        return option;
    }

    public void setOptions(HashSet<Integer> opts,M10 m){
        int i=0;
        Iterator it=opts.iterator();
        while(it.hasNext())
            m.opts[i++]=String.valueOf(it.next());
    }

    public void reset(){
        int value = 0;
        String eqn ="";
        String work="";
        String space="      ";
    }

    public void eqnEngine(int x){
        int roper=(java.lang.Math.abs(random.nextInt()))%3;
        boolean flag=false;
        if(x<=0){
            return;
        }
        else{
            if(eqn.length()==0){
                int a = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                int b = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                if(operators[roper].equals("x")||operators[roper].equals("/"))
                    flag=true;
                eqn=String.valueOf(a)+operators[roper]+String.valueOf(b);
                value=compute(a,roper,b);
            }
            else{
                int a = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                if(operators[roper].equals("x")||operators[roper].equals("/"))
                    flag=true;
                eqn+=operators[roper]+String.valueOf(a);
                value=compute(value,roper,a);
            }
            eqn="("+eqn+")";
            work+=space+eqn+"="+value+"\n";
            space=space.substring(1);
            eqnEngine(x-=1);
        }
    }


    public int compute(int a,int oper,int b){
        String op=operators[oper];
        int val=0;
        if(op.equals("+")){
            val=a+b;
        }
        else if(op.equals("-")){
            val=a-b;
        }
        else if(op.equals("x")){
            val=a*b;
        }
        else if(op.equals("/")){
            if(b!=0)
                val=a/b;
        }
        return val;
    }


    public M10 dynamicEngine(int lvl){
        String deqn="";
        String deqn1="",dwork="";
        int level = 3+lvl,oper=0;
        M10 t0=new M10();
        M10 t1=null;
        for(int i=0;i<level;i++){
            t1=tupleGenerator();

            deqn+="::"+t1.meqn;
            oper=numberGenerator()%3;
            if(deqn1.length()>0){
                deqn1="("+deqn1+operators[oper]+t1.meqn+")";
                value=compute(value, oper, t1.mvalue);
            }
            else{
                deqn1+=t1.meqn;
                value=t1.mvalue;
            }
            dwork+=deqn1+"="+value+"\n";
        }
        t0.meqn=deqn1;
        t0.mvalue=value;
        t0.mwork=dwork;
		/*System.out.println("-------------------");
		System.out.println(deqn1);
		System.out.println("-------------------");
		System.out.println(value);
		System.out.println("-------------------");
		System.out.println(dwork);
		System.out.println("-------------------");*/
        if(t0!=null)
            setOptions(options(t0.mvalue), t0);
        return t0;
    }

    public M10 tupleGenerator(){
        M10 t1= new M10();
        int simpleFlag=(java.lang.Math.abs(random.nextInt()))%2;
        if(simpleFlag==0) //0 -> simple
        {
            int rnum=numberGenerator();
            t1.mvalue=rnum;
            t1.meqn=String.valueOf(rnum);
        }
        else{  // 1 -> complex
            int r1=numberGenerator(),r2=numberGenerator();
            int oper=numberGenerator()%3;
            t1.meqn="("+r1+operators[oper]+r2+")";
            t1.mvalue=compute(r1, oper, r2);
        }
        return t1;
    }

    public int numberGenerator(){
        int nextNum=0;
        nextNum=(java.lang.Math.abs(random.nextInt()))%mod;
        return nextNum;
    }


    public String linearEngine(){

		/* rsign= (java.lang.Math.abs(random.nextInt()))%2;
		 *
		 */

        String[] operators ={"+","-","x","/"};
        java.util.Random random= new java.util.Random();
        int roper,rsign,rno_oper,rnum;
        int diff_lvl=0; /* 1,2,3 */
        int value = 0;
        String eqn ="";
        boolean flag=false;

        rsign= (java.lang.Math.abs(random.nextInt()))%2;

        for(int j=0;j<100;j++){
            rno_oper= 3 + (java.lang.Math.abs(random.nextInt()))%4;
            System.out.print("LOS "+rno_oper+"\t");
            rnum = 1 + (java.lang.Math.abs(random.nextInt()))%10; /* use level to increase value */
            eqn="a"+String.valueOf(rnum);
            for(int i=0;i<(rno_oper-1);i++){
                flag=false;
                roper= (java.lang.Math.abs(random.nextInt()))%4;
                if(operators[roper].equals("x")||operators[roper].equals("/"))
                    flag=true;
                rnum = (java.lang.Math.abs(random.nextInt()))%10;
                eqn+=operators[roper];
                if(flag){
                    eqn+=String.valueOf(rnum)+")";
                    eqn=eqn.replaceAll("a", "(");
                }
                else{
                    eqn=eqn.replaceAll("a", "");
                    eqn+="a"+String.valueOf(rnum);
                }

            }
            System.out.println(eqn);
        }
        return eqn;
    }


    public int randomSign(){
        if(numberGenerator()%2==0)
            return -1;
        return 1;
    }

    public M10 quadraticEqn(){
        M10 t1 = new M10();
        int sign=randomSign()*randomSign();
        int a=sign*(1+numberGenerator()%9);
        sign=randomSign()*randomSign();
        int b=sign*(1+numberGenerator()%9);
        sign=randomSign()*randomSign();
        int c=sign*(1+numberGenerator()%9);
        sign=randomSign()*randomSign();
        int d=sign*(1+numberGenerator()%9);

        String sign1="",sign2="";
        int A1=a*c;
        int B1=((b*c)+(a*d));
        if(B1>0)
            sign1="+";
        int C1=b*d;
        if(C1>0)
            sign2="+";
        t1.meqn=""+A1+"x#sqr# "+sign1+B1+"x "+sign2+C1;
        t1.mvalue=0;
		/*
		sign1="";
		if(b>0)
			sign1="+";
		t1.mvalues="("+a+"x "+sign1+b+")";
		sign2="";
		if(d>0)
			sign2="+";
		t1.mvalues+="("+c+"x "+sign2+d+")";

		*/

        int A=a,B=b,C=c,D=d;
        String s1="",s2="",stmp="";
        if(B>0)
            s1="+";
        if(D>0)
            s2="+";
        t1.mvalues = "("+A+"x "+s1+B+")("+C+"x "+s2+D+")";

        s1="";
        s2="";
        String[] opts= {"","","",""};
        int limit=10+(java.lang.Math.abs(random.nextInt()))%90,rnum=0,j=0,k=0,ans=0;
        rnum=1+java.lang.Math.abs(random.nextInt())%limit;
        ans=java.lang.Math.abs(random.nextInt())%4;

        for(int i=0;i<10;i++){
            s1=s2=stmp="";
            if(k<4){
                switch(j){
                    case 0:j++;A=a+rnum==0?a+rnum+rnum:a+rnum;break;
                    case 1:j++;B=b+rnum==0?b+rnum+rnum:b+rnum;break;
                    case 2:j++;C=c+rnum==0?c+rnum+rnum:c+rnum;break;
                    case 3:j=0;D=d+rnum==0?d+rnum+rnum:d+rnum;break;
                }
                if(B>0)
                    s1="+";
                if(D>0)
                    s2="+";
                stmp= "("+A+"x "+s1+B+")("+C+"x "+s2+D+")";
                if(!stmp.equals(t1.mvalues)){
                    if(k==ans)
                        stmp=t1.mvalues;
                    opts[k++]=stmp;
                }

            }
        }

        t1.opts=opts;
        return t1;
    }

    public String setQuadOptions(int a,int b,int c, int d){
        String result="";
        int A=a,B=b,C=c,D=d;
        String s1="",s2="",stmp="";
        if(B>0)
            s1="+";
        if(D>0)
            s2="+";
        result = "("+A+"x "+s1+B+")("+C+"x "+s2+D+")";

        s1="";
        s2="";
        String[] opts= {"","","",""};
        int limit=10+(java.lang.Math.abs(random.nextInt()))%90,rnum=0,j=0,k=0;
        rnum=1+java.lang.Math.abs(random.nextInt())%limit;

        for(int i=0;i<10;i++){
            s1=s2=stmp="";
            if(k<4){
                switch(j){
                    case 0:j++;A=a+rnum==0?a+rnum+rnum:a+rnum;break;
                    case 1:j++;B=b+rnum==0?b+rnum+rnum:b+rnum;break;
                    case 2:j++;C=c+rnum==0?c+rnum+rnum:c+rnum;break;
                    case 3:j=0;D=d+rnum==0?d+rnum+rnum:d+rnum;break;
                }
                if(B>0)
                    s1="+";
                if(D>0)
                    s2="+";
                stmp= "("+A+"x "+s1+B+")("+C+"x "+s2+D+")";
                if(!stmp.equals(result))
                    opts[k++]=stmp;
            }
        }
        return result;
    }

	/*
	 * area
	 */

    public M10 areaCircle(){
        int radius=(numberGenerator()+1)*10;
        M10 t1 = new M10();
        t1.mwork="#pi# r#sqr#";
        int flag=numberGenerator()%2;
        if(flag==0)
        {
            // area = pi * r * r * 100
            t1.meqn="Calculate the Area of the circle with radius "+radius+" meters";
            Double d=(pi*radius*radius);
            t1.mvalue=d.intValue();
        }
        else
        {
            Double d=pi*radius*radius;
            t1.mvalue=d.intValue();
            t1.meqn="Calculate the radius of the circle with area "+t1.mvalue+" sq meters";
            t1.mvalue=radius;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 areaSquare(){
        int length=numberGenerator();
        M10 t1 = new M10();
        t1.mwork="a x a";
        int flag=numberGenerator()%2;
        if(flag==0)
        {
            t1.meqn="Calculate the Area of the square with side of length "+length+" meters";
            t1.mvalue=length*length;
        }
        else
        {
            t1.mvalue=length*length;
            t1.meqn="Calculate the length of the square with area "+t1.mvalue+" sq meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 areaTriangle(){
        M10 t1 = new M10();
        t1.mwork="a x h / 2";
		/*todo */
        return t1;
    }

    public M10 areaPentagon(){
        int length=(1+numberGenerator())*10;
        M10 t1 = new M10();
        t1.mwork="/areaPentagon";
        int flag=numberGenerator()%2;
        Double d=(Math.sqrt((5*(5+(2*Math.sqrt(5))*length*length))))/4;
        if(flag==0)
        {
            t1.meqn="Calculate the Area of the pentagon with side of length "+length+" meters";
            t1.mvalue=d.intValue();
        }
        else
        {
            t1.mvalue=d.intValue();
            t1.meqn="Calculate the length of the pentagon with area "+t1.mvalue+" sq meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 areaHexagon(){
        int length=numberGenerator()*10;
        M10 t1 = new M10();
        t1.mwork="( 3 x #sqroot# 3 x a #sqr# ) / 2";
        int flag=numberGenerator()%2;
        Double d=2.598*length*length;
        if(flag==0)
        {
            t1.meqn="Calculate the Area of the hexagon with side of length "+length*10+" meters";
            t1.mvalue=d.intValue();
        }
        else
        {
            t1.mvalue=d.intValue();
            t1.meqn="Calculate the length of the hexagon with area "+t1.mvalue+" sq meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 areaSector(){
		/*TODO */
        int length=numberGenerator();
        M10 t1 = new M10();

        setOptions(options(t1.mvalue), t1);
        return t1;
    }


	/*
	 * perimeter
	 */

    public M10 circumfernceCircle(){
        int length=(numberGenerator()+1)*10;
        M10 t1 = new M10();
        t1.mwork="2 x #pi# x R (Radius of the circle)";
        int flag=numberGenerator()%2;
        Double d=2*pi*length;
        if(flag==0)
        {
            t1.meqn="Calculate the circumference of the circle with radius "+length+" meters";
            t1.mvalue=d.intValue();
        }
        else
        {
            t1.mvalue=d.intValue();
            t1.meqn="Calculate the radius of the circle with circumference "+t1.mvalue+" meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

	/*
	 * volume
	 */

    public M10 volumeSphere(){
        int length=(1+numberGenerator())*10;
        M10 t1 = new M10();
        t1.mwork="4 x #pi# x R#cube# / 3 (Radius of the circle)";
        int flag=numberGenerator()%2;
        Double d=(4*pi*length*length*length)/3;
        if(flag==0)
        {
            t1.meqn="Calculate the volume of the sphere with radius "+length+" meters";
            t1.mvalue=d.intValue();
        }
        else
        {
            t1.mvalue=d.intValue();
            t1.meqn="Calculate the radius of the sphere with volume "+t1.mvalue+" cubic meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 volumeCube(){
        int length=numberGenerator();
        M10 t1 = new M10();
        t1.mwork="L#cube#";
        int flag=numberGenerator()%2;
        if(flag==0)
        {
            t1.meqn="Calculate the volume of the cube with length "+length+" meters";
            t1.mvalue=length*length*length;
        }
        else
        {
            t1.mvalue=length*length*length;
            t1.meqn="Calculate the length of the cube with volume "+t1.mvalue+" cubic meters";
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }

    public M10 volumeCylinder(){
        int length=(numberGenerator()+1)*10;
        int height=numberGenerator()+1 ;
        M10 t1 = new M10();
        t1.mwork="#pi# x R#sqr# x h";
        int flag=numberGenerator()%3;
        Double d=pi*length*length*height;
        t1.mvalue=d.intValue();
        if(flag==0)
        {
            t1.meqn="Calculate the volume of the cylinder with radius "+length+" meters and height "+height+" meters";
        }
        else if(flag==1)
        {
            t1.meqn="Calculate the height of the cylinder with volume "+t1.mvalue+" cubic  meters and radius "+length;
            t1.mvalue=height;
        }
        else
        {
            t1.meqn="Calculate the radius of the cylinder with volume "+t1.mvalue+" cubic meters and height "+height;
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }


    public M10 volumeCone(){
        int length=(numberGenerator()+1)*10;
        int height=numberGenerator()+1 ;
        M10 t1 = new M10();
        t1.mwork="#pi# x R#sqr# x h / 3";
        int flag=numberGenerator()%3;
        Double d=pi*length*length*height/3;
        t1.mvalue=d.intValue();
        if(flag==0)
        {
            t1.meqn="Calculate the volume of the cone with radius "+length+" meters and height "+height+" meters";
        }
        else if(flag==1)
        {
            t1.meqn="Calculate the height of the cone with volume "+t1.mvalue+" cubic meters and radius "+length;
            t1.mvalue=height;
        }
        else
        {
            t1.meqn="Calculate the radius of the cone with volume "+t1.mvalue+" cubic meters and height "+height;
            t1.mvalue=length;
        }
        setOptions(options(t1.mvalue), t1);
        return t1;
    }



    public String strFormat(Double d){

        StringBuilder sbuf = new StringBuilder();
        Formatter fmt = new Formatter(sbuf);
        fmt.format("%f%n", Math.PI);
        return sbuf.toString();
    }

}

class M10{
    String meqn="";
    int mvalue=0;
    String mvalues="";
    String mwork="";
    String[] opts = new String[4];

    public void toStr(){
        System.out.println("--------------------");
        System.out.println(meqn);
        System.out.println(mvalue);
        System.out.println(mvalues);
        System.out.println("["+opts[0]+"] ["+opts[1]+"]");
        System.out.println("["+opts[2]+"] ["+opts[3]+"]");
        System.out.println("--------------------");
    }
}

/*
 * ((TextView)findViewById(R.id.text)).setText(Html.fromHtml("X<sup>2</sup>"));
 */


/*
package mbrass.com.hamil10;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class EqEngine {

    String[] operators ={"+","-","x"};
    java.util.Random random= new java.util.Random();
    int rsign;
    int diff_lvl=0; /* 1,2,3 *
    int value = 0;
    String eqn ="";
    String work="";
    String space="";

    public EqEngine(int x){
        diff_lvl=3+x;
        for(int i=0;i<diff_lvl;i++)
            space+=" ";
    }

    public static void main(String[] args){
        EqEngine ob = new EqEngine(1);
        ob.eqnEngine(ob.diff_lvl);
        System.out.println(ob.eqn+"="+ob.value);
        System.out.println(ob.work);
        HashSet<Integer> option=ob.options();
        Iterator it=option.iterator();
        System.out.println("++++++++++++++++++++++");
        while(it.hasNext()){
            int x=(Integer)it.next();
            String pre="";
            if(x==ob.value)
                pre="<--";
            System.out.println(x+pre);
        }
        System.out.println("++++++++++++++++++++++");
    }

    public HashMap<String, String> nextQuestion(){
        eqnEngine(MainActivity.diff_level+3);
        HashMap<String,String> retStr = new HashMap<String,String>();
        retStr.put("QSN",eqn);
        retStr.put("WORK",work);
        retStr.put("VALUE",String.valueOf(value));
        HashSet<Integer> option=options();
        Iterator it=option.iterator();
        String rstr="";
        while(it.hasNext()){
            int x=(Integer)it.next();
            rstr+=x+" ";
        }
        retStr.put("OPTION",rstr);
        reset();
        return retStr;
    }

    public HashSet<Integer> options(){
        int limit=10+(java.lang.Math.abs(random.nextInt()))%90,rnum=0;
        HashSet<Integer> option = new HashSet<Integer>();
        ArrayList<Integer> al=new ArrayList<Integer>();
        rnum=1+java.lang.Math.abs(random.nextInt())%limit;
        al.add(value+rnum);
        al.add(value-rnum+20);
        al.add(value*(rnum/10));
        al.add(value-(2*rnum));
        al.add((2*value)+rnum);
        al.add(3*rnum+value);
        al.add(5*rnum+value);
        option.add(value);
        Collections.shuffle(al);
        for(int i=0;i<al.size();i++){
            if(al.get(i)==value)
                option.add(al.get(i+1));
            else
                option.add(al.get(i));
            if(option.size()==4)
                i=100;
        }
        return option;
    }

    public void reset(){
        value = 0;
        eqn ="";
        work="";
        space="";
        for(int i=0;i<diff_lvl;i++)
            space+=" ";
    }

    public void eqnEngine(int x){
        int roper=(java.lang.Math.abs(random.nextInt()))%3;
        boolean flag=false;
        if(x<=0){
            return;
        }
        else{
            if(eqn.length()==0){
                int a = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                int b = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                if(operators[roper].equals("x")||operators[roper].equals("/"))
                    flag=true;
                eqn=String.valueOf(a)+operators[roper]+String.valueOf(b);
                value=compute(a,roper,b);
            }
            else{
                int a = 1 + (java.lang.Math.abs(random.nextInt()))%10;
                if(operators[roper].equals("x")||operators[roper].equals("/"))
                    flag=true;
                eqn+=operators[roper]+String.valueOf(a);
                value=compute(value,roper,a);
            }
            eqn="("+eqn+")";
            work+=space+eqn+"="+value+"\n";
            space=space.substring(1);
            eqnEngine(x-=1);
        }
    }

    public int compute(int a,int oper,int b){
        String op=operators[oper];
        int val=0;
        if(op.equals("+"))
            val=a+b;
        else if(op.equals("-"))
            val=a-b;
        else if(op.equals("x"))
            val=a*b;
        else if(op.equals("/")){
            if(b!=0)
                val=a/b;
        }
        return val;
    }
}
*/