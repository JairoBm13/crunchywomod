// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

public final class AnimatorSet extends Animator
{
    private ValueAnimator mDelayAnim;
    private long mDuration;
    private boolean mNeedsSort;
    private HashMap<Animator, Node> mNodeMap;
    private ArrayList<Node> mNodes;
    private ArrayList<Animator> mPlayingSet;
    private AnimatorSetListener mSetListener;
    private ArrayList<Node> mSortedNodes;
    private long mStartDelay;
    private boolean mStarted;
    boolean mTerminated;
    
    public AnimatorSet() {
        this.mPlayingSet = new ArrayList<Animator>();
        this.mNodeMap = new HashMap<Animator, Node>();
        this.mNodes = new ArrayList<Node>();
        this.mSortedNodes = new ArrayList<Node>();
        this.mNeedsSort = true;
        this.mSetListener = null;
        this.mTerminated = false;
        this.mStarted = false;
        this.mStartDelay = 0L;
        this.mDelayAnim = null;
        this.mDuration = -1L;
    }
    
    private void sortNodes() {
        if (this.mNeedsSort) {
            this.mSortedNodes.clear();
            final ArrayList<Node> list = new ArrayList<Node>();
            for (int size = this.mNodes.size(), i = 0; i < size; ++i) {
                final Node node = this.mNodes.get(i);
                if (node.dependencies == null || node.dependencies.size() == 0) {
                    list.add(node);
                }
            }
            final ArrayList<Node> list2 = new ArrayList<Node>();
            while (list.size() > 0) {
                for (int size2 = list.size(), j = 0; j < size2; ++j) {
                    final Node node2 = list.get(j);
                    this.mSortedNodes.add(node2);
                    if (node2.nodeDependents != null) {
                        for (int size3 = node2.nodeDependents.size(), k = 0; k < size3; ++k) {
                            final Node node3 = node2.nodeDependents.get(k);
                            node3.nodeDependencies.remove(node2);
                            if (node3.nodeDependencies.size() == 0) {
                                list2.add(node3);
                            }
                        }
                    }
                }
                list.clear();
                list.addAll(list2);
                list2.clear();
            }
            this.mNeedsSort = false;
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
            }
        }
        else {
            for (int size4 = this.mNodes.size(), l = 0; l < size4; ++l) {
                final Node node4 = this.mNodes.get(l);
                if (node4.dependencies != null && node4.dependencies.size() > 0) {
                    for (int size5 = node4.dependencies.size(), n = 0; n < size5; ++n) {
                        final Dependency dependency = node4.dependencies.get(n);
                        if (node4.nodeDependencies == null) {
                            node4.nodeDependencies = new ArrayList<Node>();
                        }
                        if (!node4.nodeDependencies.contains(dependency.node)) {
                            node4.nodeDependencies.add(dependency.node);
                        }
                    }
                }
                node4.done = false;
            }
        }
    }
    
    @Override
    public AnimatorSet clone() {
        final AnimatorSet set = (AnimatorSet)super.clone();
        set.mNeedsSort = true;
        set.mTerminated = false;
        set.mStarted = false;
        set.mPlayingSet = new ArrayList<Animator>();
        set.mNodeMap = new HashMap<Animator, Node>();
        set.mNodes = new ArrayList<Node>();
        set.mSortedNodes = new ArrayList<Node>();
        final HashMap<Node, Node> hashMap = new HashMap<Node, Node>();
        for (final Node node : this.mNodes) {
            final Node clone = node.clone();
            hashMap.put(node, clone);
            set.mNodes.add(clone);
            set.mNodeMap.put(clone.animation, clone);
            clone.dependencies = null;
            clone.tmpDependencies = null;
            clone.nodeDependents = null;
            clone.nodeDependencies = null;
            final ArrayList<AnimatorListener> listeners = clone.animation.getListeners();
            if (listeners != null) {
                ArrayList<AnimatorListener> list = null;
                for (final AnimatorListener animatorListener : listeners) {
                    if (animatorListener instanceof AnimatorSetListener) {
                        ArrayList<AnimatorListener> list2;
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<AnimatorListener>();
                        }
                        list2.add(animatorListener);
                        list = list2;
                    }
                }
                if (list == null) {
                    continue;
                }
                final Iterator<AnimatorListener> iterator3 = list.iterator();
                while (iterator3.hasNext()) {
                    listeners.remove(iterator3.next());
                }
            }
        }
        for (final Node node2 : this.mNodes) {
            final Node node3 = hashMap.get(node2);
            if (node2.dependencies != null) {
                for (final Dependency dependency : node2.dependencies) {
                    node3.addDependency(new Dependency((Node)hashMap.get(dependency.node), dependency.rule));
                }
            }
        }
        return set;
    }
    
    public Builder play(final Animator animator) {
        if (animator != null) {
            this.mNeedsSort = true;
            return new Builder(animator);
        }
        return null;
    }
    
    public void playTogether(final Animator... array) {
        if (array != null) {
            this.mNeedsSort = true;
            final Builder play = this.play(array[0]);
            for (int i = 1; i < array.length; ++i) {
                play.with(array[i]);
            }
        }
    }
    
    @Override
    public void start() {
        this.mTerminated = false;
        this.mStarted = true;
        this.sortNodes();
        final int size = this.mSortedNodes.size();
        for (int i = 0; i < size; ++i) {
            final Node node = this.mSortedNodes.get(i);
            final ArrayList<AnimatorListener> listeners = node.animation.getListeners();
            if (listeners != null && listeners.size() > 0) {
                for (final AnimatorListener animatorListener : new ArrayList<AnimatorListener>((Collection<? extends AnimatorListener>)listeners)) {
                    if (animatorListener instanceof DependencyListener || animatorListener instanceof AnimatorSetListener) {
                        node.animation.removeListener(animatorListener);
                    }
                }
            }
        }
        final ArrayList<Node> list = new ArrayList<Node>();
        for (int j = 0; j < size; ++j) {
            final Node node2 = this.mSortedNodes.get(j);
            if (this.mSetListener == null) {
                this.mSetListener = new AnimatorSetListener(this);
            }
            if (node2.dependencies == null || node2.dependencies.size() == 0) {
                list.add(node2);
            }
            else {
                for (int size2 = node2.dependencies.size(), k = 0; k < size2; ++k) {
                    final Dependency dependency = node2.dependencies.get(k);
                    dependency.node.animation.addListener((AnimatorListener)new DependencyListener(this, node2, dependency.rule));
                }
                node2.tmpDependencies = (ArrayList<Dependency>)node2.dependencies.clone();
            }
            node2.animation.addListener((AnimatorListener)this.mSetListener);
        }
        if (this.mStartDelay <= 0L) {
            for (final Node node3 : list) {
                node3.animation.start();
                this.mPlayingSet.add(node3.animation);
            }
        }
        else {
            (this.mDelayAnim = ValueAnimator.ofFloat(0.0f, 1.0f)).setDuration(this.mStartDelay);
            this.mDelayAnim.addListener((AnimatorListener)new AnimatorListenerAdapter() {
                boolean canceled = false;
                
                @Override
                public void onAnimationEnd(final Animator animator) {
                    if (!this.canceled) {
                        for (int size = list.size(), i = 0; i < size; ++i) {
                            final Node node = list.get(i);
                            node.animation.start();
                            AnimatorSet.this.mPlayingSet.add(node.animation);
                        }
                    }
                }
            });
            this.mDelayAnim.start();
        }
        if (this.mListeners != null) {
            final ArrayList list2 = (ArrayList)this.mListeners.clone();
            for (int size3 = list2.size(), l = 0; l < size3; ++l) {
                list2.get(l).onAnimationStart(this);
            }
        }
        if (this.mNodes.size() == 0 && this.mStartDelay == 0L) {
            this.mStarted = false;
            if (this.mListeners != null) {
                final ArrayList list3 = (ArrayList)this.mListeners.clone();
                for (int size4 = list3.size(), n = 0; n < size4; ++n) {
                    list3.get(n).onAnimationEnd(this);
                }
            }
        }
    }
    
    private class AnimatorSetListener implements AnimatorListener
    {
        private AnimatorSet mAnimatorSet;
        
        AnimatorSetListener(final AnimatorSet mAnimatorSet) {
            this.mAnimatorSet = mAnimatorSet;
        }
        
        @Override
        public void onAnimationEnd(final Animator animator) {
            animator.removeListener((AnimatorListener)this);
            AnimatorSet.this.mPlayingSet.remove(animator);
            this.mAnimatorSet.mNodeMap.get(animator).done = true;
            if (!AnimatorSet.this.mTerminated) {
                final ArrayList access$200 = this.mAnimatorSet.mSortedNodes;
                final boolean b = true;
                final int size = access$200.size();
                int n = 0;
                boolean b2;
                while (true) {
                    b2 = b;
                    if (n >= size) {
                        break;
                    }
                    if (!access$200.get(n).done) {
                        b2 = false;
                        break;
                    }
                    ++n;
                }
                if (b2) {
                    if (AnimatorSet.this.mListeners != null) {
                        final ArrayList list = (ArrayList)AnimatorSet.this.mListeners.clone();
                        for (int size2 = list.size(), i = 0; i < size2; ++i) {
                            list.get(i).onAnimationEnd(this.mAnimatorSet);
                        }
                    }
                    this.mAnimatorSet.mStarted = false;
                }
            }
        }
        
        @Override
        public void onAnimationRepeat(final Animator animator) {
        }
        
        @Override
        public void onAnimationStart(final Animator animator) {
        }
    }
    
    public class Builder
    {
        private Node mCurrentNode;
        
        Builder(final Animator animator) {
            this.mCurrentNode = AnimatorSet.this.mNodeMap.get(animator);
            if (this.mCurrentNode == null) {
                this.mCurrentNode = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, this.mCurrentNode);
                AnimatorSet.this.mNodes.add(this.mCurrentNode);
            }
        }
        
        public Builder with(final Animator animator) {
            Node node;
            if ((node = AnimatorSet.this.mNodeMap.get(animator)) == null) {
                node = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 0));
            return this;
        }
    }
    
    private static class Dependency
    {
        public Node node;
        public int rule;
        
        public Dependency(final Node node, final int rule) {
            this.node = node;
            this.rule = rule;
        }
    }
    
    private static class DependencyListener implements AnimatorListener
    {
        private AnimatorSet mAnimatorSet;
        private Node mNode;
        private int mRule;
        
        public DependencyListener(final AnimatorSet mAnimatorSet, final Node mNode, final int mRule) {
            this.mAnimatorSet = mAnimatorSet;
            this.mNode = mNode;
            this.mRule = mRule;
        }
        
        private void startIfReady(final Animator animator) {
            if (!this.mAnimatorSet.mTerminated) {
                final Dependency dependency = null;
                final int size = this.mNode.tmpDependencies.size();
                int n = 0;
                Dependency dependency2;
                while (true) {
                    dependency2 = dependency;
                    if (n >= size) {
                        break;
                    }
                    dependency2 = this.mNode.tmpDependencies.get(n);
                    if (dependency2.rule == this.mRule && dependency2.node.animation == animator) {
                        animator.removeListener((AnimatorListener)this);
                        break;
                    }
                    ++n;
                }
                this.mNode.tmpDependencies.remove(dependency2);
                if (this.mNode.tmpDependencies.size() == 0) {
                    this.mNode.animation.start();
                    this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
                }
            }
        }
        
        @Override
        public void onAnimationEnd(final Animator animator) {
            if (this.mRule == 1) {
                this.startIfReady(animator);
            }
        }
        
        @Override
        public void onAnimationRepeat(final Animator animator) {
        }
        
        @Override
        public void onAnimationStart(final Animator animator) {
            if (this.mRule == 0) {
                this.startIfReady(animator);
            }
        }
    }
    
    private static class Node implements Cloneable
    {
        public Animator animation;
        public ArrayList<Dependency> dependencies;
        public boolean done;
        public ArrayList<Node> nodeDependencies;
        public ArrayList<Node> nodeDependents;
        public ArrayList<Dependency> tmpDependencies;
        
        public Node(final Animator animation) {
            this.dependencies = null;
            this.tmpDependencies = null;
            this.nodeDependencies = null;
            this.nodeDependents = null;
            this.done = false;
            this.animation = animation;
        }
        
        public void addDependency(final Dependency dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<Dependency>();
                this.nodeDependencies = new ArrayList<Node>();
            }
            this.dependencies.add(dependency);
            if (!this.nodeDependencies.contains(dependency.node)) {
                this.nodeDependencies.add(dependency.node);
            }
            final Node node = dependency.node;
            if (node.nodeDependents == null) {
                node.nodeDependents = new ArrayList<Node>();
            }
            node.nodeDependents.add(this);
        }
        
        public Node clone() {
            try {
                final Node node = (Node)super.clone();
                node.animation = this.animation.clone();
                return node;
            }
            catch (CloneNotSupportedException ex) {
                throw new AssertionError();
            }
        }
    }
}
