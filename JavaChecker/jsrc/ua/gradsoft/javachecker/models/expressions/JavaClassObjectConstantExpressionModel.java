/*
 * JavaClassObjectConstantExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.models.JavaClassTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaPlaceContextFactory;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.trace.JavaTraceContext;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *'Expression'  which is really a constant, situated in class
 * @author rssh
 */
public class JavaClassObjectConstantExpressionModel implements JavaObjectConstantExpressionModel
{
    
    /** Creates a new instance of JavaClassObjectConstantExpressionModel */
    public JavaClassObjectConstantExpressionModel(Object o, JavaTypeModel enclosedType) {
        o_=o;
        enclosedType_=enclosedType;
    }
    
    
    public JavaTypeModel  getType() throws TermWareException
    {
        return JavaClassTypeModel.createTypeModel(o_.getClass());
    }
    
    public boolean isType()
    { return false; }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.OBJECT_CONSTANT;  
    }
   
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.emptyList(); }
    
    public JavaTypeModel getEnclosedType()
    { return enclosedType_; }
    
    public JavaStatementModel  getStatementModel()
    { return null; }
    
    public Object getConstant()
    { return o_; }
 
    public Term getModelTerm() throws TermWareException
    {
        JavaPlaceContext ctx;
        if (enclosedType_!=null) {
            ctx = JavaPlaceContextFactory.createNewTypeContext(enclosedType_);
        }else{
            ctx = JavaPlaceContextFactory.createNewPackageContext("java.lang");
        }
        Term x1 = TermUtils.createJTerm(o_);
        Term x2 = TermUtils.createJTerm(ctx);
        Term retval = TermUtils.createTerm("ObjectConstantExpressionModel",x1,x2);
        return retval;
    }


    public Term getTerm() throws TermWareException
    {
        if (o_ instanceof Serializable) {
            Serializable so = (Serializable)o_;
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            try{
              ObjectOutputStream oo = new ObjectOutputStream(ba);
              oo.writeObject(so);
              oo.flush();
            }catch(IOException ex){
                throw new AssertException("Exception during serialization of constant",ex);
            }
            byte[] bytes = ba.toByteArray();

            //Object o = ua.gradsoft.java.Helper.readSerializedObject(o.class, ba);
            
            Term listTerm = TermUtils.createNil();
            Term byteTypeTerm = TermUtils.createAtom("byte");
            for(int i=0; i<bytes.length; ++i) {
                Term literal = TermUtils.createTerm("IntegerLiteral",
                                 TermUtils.createString(""+bytes[i]));
                Term cast = TermUtils.createTerm("CastExpression",
                                                 byteTypeTerm,literal);
                listTerm = TermUtils.createTerm("cons",cast,listTerm);
            }
            listTerm = TermUtils.reverseListTerm(listTerm);

            Term byteArrayTerm = TermUtils.createTerm("ArrayInitializer",listTerm);

            
            Term nameTerm = TermUtils.createTerm("cons",
                    TermUtils.createIdentifier("ua"),
                     TermUtils.createTerm("cons",
                      TermUtils.createIdentifier("gradsoft"),
                       TermUtils.createTerm("cons",
                        TermUtils.createIdentifier("java"),
                         TermUtils.createTerm("cons",
                          TermUtils.createIdentifier("Helper"),
                           TermUtils.createTerm("cons",
                            TermUtils.createIdentifier("readSerializedObject"),
                             TermUtils.createNil()
                                     )    )  )  )   );

            Term argumentsTerm = TermUtils.createTerm("Arguments",
                                  TermUtils.createTerm("ClassLiteral",
                                   JavaClassTypeModel.createTypeModel(o_.getClass()).getFullNameAsTerm()
                                  ),
                                  byteArrayTerm
                                 );

            Term t = TermUtils.createTerm("MethodCall",nameTerm, argumentsTerm);

            return t;
        } else {
            throw new AssertException("Can't dump non-serializable object to external term");
        }
    }
    
    public boolean isConstantExpression()
    { return true; }
    
    public JavaExpressionModel eval(JavaTraceContext ctx)
    {
        return this;
    }
    
    private Object o_;
    private JavaTypeModel enclosedType_;
}
