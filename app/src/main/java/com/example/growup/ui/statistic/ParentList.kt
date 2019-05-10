package com.example.growup.ui.statistic

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class ParentList(title: String, items: List<ChildList>) : ExpandableGroup<ChildList>(title, items)