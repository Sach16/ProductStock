package com.skpissay.productstock.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skpissay.productstock.baseclasses.DuskFragmentBaseClass;
import com.skpissay.productstock.fragments.PotUserHomeDuskFragment;
import com.skpissay.productstock.fragments.PotUserHomeDuskFragment2;
import com.skpissay.productstock.fragments.PotUserHomeDuskFragment3;

/**
 * Created by S.K. Pissay on 17/05/17.
 */
public class PagerAdapterForDusk extends FragmentStatePagerAdapter {

    int m_cNumOfTabs;
    String m_cId;

    //write inner fragment items below

    public DuskFragmentBaseClass m_cObjFragmentBase;

    public PagerAdapterForDusk(FragmentManager pFragment, DuskFragmentBaseClass pObjFragmentBase,
                                     int pNumOfTabs, String pId) {
        super(pFragment);
        this.m_cNumOfTabs = pNumOfTabs;
        this.m_cObjFragmentBase = pObjFragmentBase;
        this.m_cId = pId;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                m_cObjFragmentBase = PotUserHomeDuskFragment.newInstance(position, m_cId);
                return m_cObjFragmentBase;
            case 1:
                m_cObjFragmentBase = PotUserHomeDuskFragment2.newInstance(position, m_cId);
                return m_cObjFragmentBase;
            case 2:
                m_cObjFragmentBase = PotUserHomeDuskFragment3.newInstance(position, m_cId);
                return m_cObjFragmentBase;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return m_cNumOfTabs;
    }
}
