package com.growup.growup.data.statistic.model

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class ParentList(title: String, items: List<ChildList>) : ExpandableGroup<ChildList>(title, items)